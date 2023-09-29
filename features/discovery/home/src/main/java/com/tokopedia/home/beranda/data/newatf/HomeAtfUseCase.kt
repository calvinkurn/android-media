package com.tokopedia.home.beranda.data.newatf

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
    var job: Job? = null

    private val _flow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    val flow: StateFlow<AtfDataList?>
        get() = _flow

    private val atfFlows: List<StateFlow<AtfData?>> = listOf(
        tickerRepository.flow,
        homepageBannerRepository.flow,
        dynamicIconRepository.flow,
        missionWidgetRepository.flow,
        todoWidgetRepository.flow,
        atfChannelRepository.flow,
    )

    /**
     * Initial ATF data fetching (cache + remote)
     */
    suspend fun fetchAtfDataList() {
        coroutineScope {
            //only fetch dynamic position on first load
            job = launch(homeDispatcher.io) {
                dynamicPositionRepository.getData()
            }
            observeDynamicPositionFlow()
            observeAtfComponentFlow()
        }
    }

    /**
     * Refresh ATF data (remote only)
     */
    suspend fun refreshData() {
        coroutineScope {
            if(job?.isActive != true) {
                job = launch(homeDispatcher.io) {
                    dynamicPositionRepository.getRemoteData()
                }
            }
        }
    }

    /**
     * Collecting dynamic position flow.
     * - If Dynamic Position status is success & all ATF components are not in loading state,
     *   then emit to flow and save to cache
     * - Fetch each ATF components if needed.
     */
    private fun CoroutineScope.observeDynamicPositionFlow() {
        launch(homeDispatcher.io) {
            dynamicPositionRepository.flow.collect { value ->
                if(value != null) {
                    if(value.isDataReady()) {
                        launch { emitAndSave(value) }
                    }
                    if(value.needToFetchComponents) {
                        launch(homeDispatcher.io) {
                            getEachAtfComponentData(value)
                        }
                    }
                }
            }
        }
    }

    /**
     * To emit latest ATF data and save it to cache
     */
    private suspend fun emitAndSave(value: AtfDataList) {
        _flow.emit(value)
        dynamicPositionRepository.saveLatestAtf(value)
    }

    /**
     * Alternative 3: combine all flows into one, but always update whenever data changes
     * Cons: could be heavier to when mapping
     */
    private fun CoroutineScope.observeAtfComponentFlow() {
        launch {
            val allFlows: List<Flow<Any?>> = listOf(dynamicPositionRepository.flow) + atfFlows
            combine(allFlows) { list ->
                val dynamicPos = list[0] as? AtfDataList
                val listAtfData = list.drop(1) as List<AtfData?>
                if(dynamicPos != null && dynamicPos.isPositionReady()) {
                    val latest = dynamicPos.updateAtfContents(listAtfData)
                    launch { emitAndSave(latest) }
                }
            }.launchIn(this)
        }
    }

    /**
     * Fetch data for each ATF components
     */
    private suspend fun getEachAtfComponentData(value: AtfDataList) {
        coroutineScope {
            launch {
                value.listAtfData.forEach { data ->
                    val metadata = data.atfMetadata
                    when(metadata.component) {
                        AtfKey.TYPE_BANNER -> launch { homepageBannerRepository.getData(metadata) }
                        AtfKey.TYPE_ICON -> launch { dynamicIconRepository.getData(metadata) }
                        AtfKey.TYPE_TICKER -> launch { tickerRepository.getData(metadata) }
                        AtfKey.TYPE_CHANNEL -> launch { atfChannelRepository.getData(metadata) }
                        AtfKey.TYPE_MISSION -> launch { missionWidgetRepository.getData(metadata) }
                        AtfKey.TYPE_TODO -> launch { todoWidgetRepository.getData(metadata) }
                    }
                }
            }
        }
    }
}
