package com.tokopedia.home.beranda.data.newatf

import android.util.Log
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerRepository
import com.tokopedia.home.beranda.data.newatf.channel.AtfChannelRepository
import com.tokopedia.home.beranda.data.newatf.icon.DynamicIconRepository
import com.tokopedia.home.beranda.data.newatf.mission.MissionWidgetRepository
import com.tokopedia.home.beranda.data.newatf.position.DynamicPositionRepository
import com.tokopedia.home.beranda.data.newatf.ticker.TickerRepository
import com.tokopedia.home.beranda.data.newatf.todo.TodoWidgetRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.constant.AtfKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HomeScope
class HomeAtfUseCase @Inject constructor(
    private val homeDispatcher: CoroutineDispatchers,
    private val dynamicPositionRepository: DynamicPositionRepository,
    private val homepageBannerRepository: HomepageBannerRepository,
    private val dynamicIconRepository: DynamicIconRepository,
    private val tickerRepository: TickerRepository,
    private val atfChannelRepository: AtfChannelRepository,
    private val missionWidgetRepository: MissionWidgetRepository,
    private val todoWidgetRepository: TodoWidgetRepository,
) {
    var job: Job? = null

    val flow: StateFlow<AtfDataList?>
        get() = _flow
    private var _flow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)

    private val atfFlowList: List<StateFlow<AtfData?>> = listOf(
        tickerRepository.flow,
        homepageBannerRepository.flow,
        dynamicIconRepository.flow,
        missionWidgetRepository.flow,
        todoWidgetRepository.flow,
        atfChannelRepository.flow,
    )

    private val atfFlowListName: List<String> = listOf(
        "tickerRepository.flow",
        "homepageBannerRepository.flow",
        "dynamicIconRepository.flow",
        "missionWidgetRepository.flow",
        "todoWidgetRepository.flow",
        "atfChannelRepository.flow",
    )

    suspend fun fetchAtfDataList() {
        Log.d("atfflow", "3. HomeAtfUseCase - fetchAtfDataList")
        coroutineScope {
            //only fetch dynamic position on first load
            job = launch { dynamicPositionRepository.getData() }
            observeDynamicPositionFlow()
            observeAtfComponentFlow()
        }
    }

    suspend fun refreshData() {
        Log.d("atfflow", "refreshData: ")
        coroutineScope {
            if(job?.isActive != true) {
                Log.d("atfflow", "refreshData: should not be called unless job is done")
                job = launch { dynamicPositionRepository.getRemoteData() }
            }
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

    private fun CoroutineScope.observeDynamicPositionFlow() {
        launch {
            dynamicPositionRepository.flow.collect { value ->
                Log.d("atfflow", "5. HomeAtfUseCase : observeDynamicPositionFlow $value")
                if(value != null) {
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

    private fun CoroutineScope.observeAtfComponentFlow() {
        atfFlowList.forEachIndexed { idx, stateFlow ->
            launch {
                flow.combine(stateFlow) { dynamicPos, atfData ->
                    Log.d("atfflow", "6. HomeAtfUseCase : observeAtfComponentFlow ${atfFlowListName[idx]} $atfData")
                    // updating flow with remote atf data should be done after
                    // dynamic position coming from remote to avoid multiple updating data
                    if(dynamicPos != null && !dynamicPos.isCache && atfData != null) {
                        dynamicPos.let { currentAtf ->
                            //first layer should check if dynamic position already from remote
                            val model = currentAtf.listAtfData.find { it.atfMetadata == atfData.atfMetadata } ?: return@combine
                            val index = currentAtf.listAtfData.indexOf(model)
                            val newList = currentAtf.listAtfData.toMutableList().apply {
                                set(index, model.copy(atfContent = atfData.atfContent))
                            }
                            val newModel = currentAtf.copy(listAtfData = newList)
                            _flow.emit(newModel)
                            Log.d("atfflow", "6. HomeAtfUseCase : observeAtfComponentFlow ${atfFlowListName[idx]} emit $flow with $newModel")
                        }
                    }
                }.launchIn(CoroutineScope(homeDispatcher.io))
            }
        }
    }
}
