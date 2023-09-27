package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerRepository
import com.tokopedia.home.beranda.data.newatf.channel.AtfChannelRepository
import com.tokopedia.home.beranda.data.newatf.icon.DynamicIconRepository
import com.tokopedia.home.beranda.data.newatf.mission.MissionWidgetRepository
import com.tokopedia.home.beranda.data.newatf.position.DynamicPositionRepository
import com.tokopedia.home.beranda.data.newatf.ticker.TickerRepository
import com.tokopedia.home.beranda.data.newatf.todo.TodoWidgetRepository
import com.tokopedia.home.constant.AtfKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeAtfUseCase @Inject constructor(
    private val dynamicPositionRepository: DynamicPositionRepository,
    private val homepageBannerRepository: HomepageBannerRepository,
    private val dynamicIconRepository: DynamicIconRepository,
    private val tickerRepository: TickerRepository,
    private val atfChannelRepository: AtfChannelRepository,
    private val missionWidgetRepository: MissionWidgetRepository,
    private val todoWidgetRepository: TodoWidgetRepository,
) {
    private var _flow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    val flow: StateFlow<AtfDataList?> = _flow

    private fun getAtfFlowList(): List<StateFlow<AtfData?>> = listOf(
        tickerRepository.flow,
        homepageBannerRepository.flow,
        dynamicIconRepository.flow,
        missionWidgetRepository.flow,
        todoWidgetRepository.flow,
        atfChannelRepository.flow,
    )

    suspend fun fetchAtfDataList() {
        coroutineScope {
            observeAtfFlow()
            observeDynamicPositionFlow()
        }
    }

    private suspend fun updateSourceOnly(isCache: Boolean) {
        val currentData = flow.value
        _flow.emit(currentData?.copy(isCache = isCache, isDifferentPosition = false))
    }

    private suspend fun updateDynamicPosition(dynamicPosition: AtfDataList) {
        if(flow.value == null) {
            _flow.emit(dynamicPosition)
        } else {
            //if from remote, update metadata and source but keep the cached content
            if(!dynamicPosition.isCache) {
                flow.value?.listAtfData?.let {
                    val copiedContent = dynamicPosition.copyAtfContents(
                        it.map { it.atfContent }
                    )
                    _flow.emit(copiedContent)
                }
            } else {
                _flow.emit(dynamicPosition)
            }
        }
    }

    private suspend fun getEachAtfComponentData(value: AtfDataList) {
        value.listAtfData.forEach { data ->
            val metadata = data.atfMetadata
            when(metadata.component) {
                AtfKey.TYPE_BANNER -> homepageBannerRepository.getData(metadata)
                AtfKey.TYPE_ICON -> dynamicIconRepository.getData(metadata)
                AtfKey.TYPE_TICKER -> tickerRepository.getData(metadata)
                AtfKey.TYPE_CHANNEL -> atfChannelRepository.getData(metadata)
                AtfKey.TYPE_MISSION -> missionWidgetRepository.getData(metadata)
                AtfKey.TYPE_TODO -> todoWidgetRepository.getData(metadata)
            }
        }
    }

    private fun CoroutineScope.observeAtfFlow() {
        getAtfFlowList().forEach { stateFlow ->
            launch {
                flow.combine(stateFlow) { dynamicPos, atfData ->
                    // updating flow with remote atf data should be done after
                    // dynamic position coming from remote to avoid multiple updating data
                    if(dynamicPos != null && !dynamicPos.isCache && atfData != null) {
                        flow.value?.let { currentAtf ->
                            //first layer should check if dynamic position already from remote
                            val model = currentAtf.listAtfData.find { it.atfMetadata == atfData?.atfMetadata } ?: return@combine
                            val index = currentAtf.listAtfData.indexOf(model)
                            val newList = currentAtf.listAtfData.toMutableList().apply {
                                set(index, model.copy(atfContent = atfData.atfContent))
                            }
                            val newModel = currentAtf.copy(listAtfData = newList)
                            _flow.emit(newModel)
                        }
                    }
                }.launchIn(CoroutineScope(Dispatchers.Main))
            }
        }
    }

    private fun CoroutineScope.observeDynamicPositionFlow() {
        launch {
            dynamicPositionRepository.flow.collect { value ->
                if(value == null) {
                    //only fetch dynamic position on first load
                    launch { dynamicPositionRepository.getData() }
                } else {
                    //if dynamic position remains the same, only update the source
                    if(flow.value?.hasSamePosition(value.listAtfData) == true) {
                        launch { updateSourceOnly(value.isCache) }
                    } else {
                        //if returns different positions, update the whole position
                        //and fetch data for each
                        launch { updateDynamicPosition(value) }
                        launch { getEachAtfComponentData(value) }
                    }
                }
            }
        }
    }
}
