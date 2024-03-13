package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.data.newatf.banner.HomepageBannerRepository
import com.tokopedia.home.beranda.data.newatf.channel.AtfChannelRepository
import com.tokopedia.home.beranda.data.newatf.icon.DynamicIconRepository
import com.tokopedia.home.beranda.data.newatf.mission.MissionWidgetRepository
import com.tokopedia.home.beranda.data.newatf.ticker.TickerRepository
import com.tokopedia.home.beranda.data.newatf.todo.TodoWidgetRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.constant.AtfKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Frenzel
 */
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
    private val scope = CoroutineScope(homeDispatcher.io)
    private var workerJob: Job? = null
    private var positionObserverJob: Job? = null
    private var atfDataObserverJob: Job? = null

    private val _flow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    val flow: StateFlow<AtfDataList?>
        get() = _flow

    private val atfFlows: List<StateFlow<List<AtfData?>>> = listOf(
        tickerRepository.flow,
        homepageBannerRepository.flow,
        dynamicIconRepository.flow,
        missionWidgetRepository.flow,
        todoWidgetRepository.flow,
        atfChannelRepository.flow
    )

    /**
     * Initial ATF data fetching (cache + remote)
     */
    suspend fun fetchAtfDataList() {
        coroutineScope {
            // only fetch dynamic position on first load
            workerJob = launch(homeDispatcher.io) {
                dynamicPositionRepository.getData()
            }
            observeDynamicPositionFlow()
            observeAtfComponentFlow()
        }
    }

    /**
     * Refresh Full ATF data (dynamic position and each ATF components) from remote.
     */
    suspend fun refreshData() {
        coroutineScope {
            if (workerJob?.isActive != true) {
                workerJob = launch(homeDispatcher.io) {
                    dynamicPositionRepository.getRemoteData(isRefresh = true)
                }
            }
        }
    }

    /**
     * Refresh specific ATF component data from remote.
     */
    suspend fun refreshData(id: String) {
        coroutineScope {
            if (workerJob?.isActive != true) {
                launch(homeDispatcher.io) {
                    flow.value?.let { value ->
                        value.listAtfData.firstOrNull { atfData ->
                            atfData.atfMetadata.id.toString() == id
                        }?.let { atfData ->
                            conditionalFetchAtfData(atfData.atfMetadata)
                        }
                    }
                }
            }
        }
    }

    /**
     * Collecting dynamic position flow.
     * - If Dynamic Position status is success & not empty,
     *   then emit to flow and save to cache
     * - Fetch each ATF components if needed.
     */
    private fun CoroutineScope.observeDynamicPositionFlow() {
        if (positionObserverJob?.isActive == true) return
        positionObserverJob = launch(homeDispatcher.io) {
            dynamicPositionRepository.flow.collect { value ->
                if (value != null) {
                    if (value.isPositionReady() || value.isDataError()) {
                        launch { emit(value) }
                    }
                    if (value.needToFetchComponents) {
                        value.listAtfData.forEach { data ->
                            conditionalFetchAtfData(data.atfMetadata)
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * Combine all flows into one ATF list, always be updated whenever data changes
     */
    private fun CoroutineScope.observeAtfComponentFlow() {
        if (atfDataObserverJob?.isActive == true) return
        val allFlows: List<Flow<Any?>> = listOf(dynamicPositionRepository.flow) + atfFlows
        atfDataObserverJob = combine(allFlows) { list ->
            // first flow is for dynamic position
            val dynamicPos = list[0] as? AtfDataList
            // other flows defined on atfFlows list
            val listAtfData = (list.drop(1) as List<List<AtfData?>>).flatten()
            // if remote dynamic position is ready, populate data to list
            if (dynamicPos != null && dynamicPos.isPositionReady() && !dynamicPos.isCache && listAtfData.isNotEmpty()) {
                val latest = dynamicPos.updateAtfContents(listAtfData)
                launch { emitAndSave(latest) }
            }
        }.launchIn(scope)
    }

    /**
     * Fetch data for each ATF components
     */
    private fun CoroutineScope.conditionalFetchAtfData(metadata: AtfMetadata) {
        when (metadata.component) {
            AtfKey.TYPE_BANNER, AtfKey.TYPE_BANNER_V2 -> launch { homepageBannerRepository.getData(metadata) }
            AtfKey.TYPE_ICON, AtfKey.TYPE_ICON_V2 -> launch { dynamicIconRepository.getData(metadata) }
            AtfKey.TYPE_TICKER -> launch { tickerRepository.getData(metadata) }
            AtfKey.TYPE_CHANNEL -> launch { atfChannelRepository.getData(metadata) }
            AtfKey.TYPE_MISSION,
            AtfKey.TYPE_MISSION_V2,
            AtfKey.TYPE_MISSION_V3 -> launch { missionWidgetRepository.getData(metadata) }
            AtfKey.TYPE_TODO -> launch { todoWidgetRepository.getData(metadata) }
        }
    }

    private suspend fun emit(value: AtfDataList) {
        _flow.emit(value)
    }

    private suspend fun emitAndSave(value: AtfDataList) {
        emit(value)
        dynamicPositionRepository.saveLatestAtf(value)
    }
}
