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
import kotlinx.coroutines.flow.Flow
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
    private val todoWidgetRepository: TodoWidgetRepository
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
        atfChannelRepository.flow
    )

    /**
     * Initial ATF data fetching
     */
    suspend fun fetchAtfDataList() {
        Log.d("atfflow", "3. HomeAtfUseCase - fetchAtfDataList")
        coroutineScope {
            // only fetch dynamic position on first load
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
    suspend fun refreshData(): Boolean {
        Log.d("atfflow", "refreshData: ")
        coroutineScope {
            Log.d("atfflow", "refreshData: should not be called unless job is done")
            return@coroutineScope dynamicPositionRepository.getRemoteData()
        }
        return false
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
                Log.d("atfflow", "5. HomeAtfUseCase : observeDynamicPositionFlow $value")
                if (value != null) {
                    if (value.isDataReady()) {
                        launch { emitAndSave(value) }
                        Log.d("atfflow", "5. HomeAtfUseCase : observeDynamicPositionFlow emit $value")
                    }
                    if (value.needToFetchComponents) {
                        launch(homeDispatcher.io) {
                            getEachAtfComponentData(value)
                        }
                    }
                    if (value.isDataError()) {
                        launch { _flow.emit(value) }
                    }
                }
            }
        }
    }

    /**
     * To emit latest ATF data and save it to cache
     */
    private suspend fun emitAndSave(value: AtfDataList) {
        Log.d("atfflow", "emitAndSave: ${value.listAtfData.map { it.atfContent }}")
        _flow.emit(value)
        dynamicPositionRepository.saveLatestAtf(value)
    }

//    /**
//     * Alternative 1: observe each atf components, update independently when data is ready
//     * Cons: could cause racing condition when updating the list in parallel
//     */
//    private fun CoroutineScope.observeAtfComponentFlow() {
//        atfFlows.forEachIndexed { idx, atfFlow ->
//            launch {
//                dynamicPositionRepository.flow.combine(atfFlow) { dynamicPos, atfData ->
//                    Log.d("atfflow", "7. HomeAtfUseCase : observeAtfComponentFlow $atfData")
//                    // updating flow with remote atf data should be done after
//                    // dynamic position coming from remote to avoid multiple updating data
//                    if(dynamicPos != null && !dynamicPos.isCache && atfData != null) {
//                        dynamicPos.let { currentAtf ->
//                            val index = currentAtf.listAtfData.indexOfFirst { it.atfMetadata == atfData.atfMetadata }
//                            if(index != -1) {
//                                try {
//                                    val newList = currentAtf.listAtfData.toMutableList().apply {
//                                        set(index, atfData)
//                                    }
//                                    Log.d("atfflow", "7. HomeAtfUseCase : observeAtfComponentFlow" +
//                                        "before: ${currentAtf.listAtfData}\n" +
//                                        "update index: $index\n" +
//                                        "after: $newList")
//                                    _flow.emit(currentAtf.copy(listAtfData = newList))
//                                } catch (_: Exception) {
//                                    Log.d("atfflow", "7. HomeAtfUseCase : observeAtfComponentFlow catch")
//                                }
//                            }
//                        }
//                    }
//                }.launchIn(CoroutineScope(homeDispatcher.io))
//            }
//        }
//    }

//    /**
//     * Alternative 2: combine all flows into one, only update when all data completed
//     * Cons: longer to update with actual data
//     */
//    private fun CoroutineScope.observeAtfComponentFlow() {
//        val allFlows: List<Flow<Any?>> = listOf(dynamicPositionRepository.flow) + atfFlows
//        combine(allFlows) { list ->
//            val dynamicPos = list[0] as? AtfDataList
//            val listAtfData = list.drop(1) as List<AtfData?>
//            val hasAllAtfComplete = listAtfData.all { it == null || it.atfContent != null }
//            if(dynamicPos != null && !dynamicPos.isCache && hasAllAtfComplete) {
//                dynamicPos.let { currentAtf ->
//                    val newList = currentAtf.listAtfData.toMutableList()
//                    listAtfData.forEach { atfData ->
//                        val model = currentAtf.listAtfData.find { it.atfMetadata == atfData?.atfMetadata } ?: return@combine
//                        val index = currentAtf.listAtfData.indexOf(model)
//                        newList[index] = model.copy(atfContent = atfData?.atfContent)
//                    }
//                    val newModel = currentAtf.copy(listAtfData = newList)
//                    emitAndSave(newModel)
//                }
//            }
//        }.launchIn(this)
//    }

    /**
     * Alternative 3: combine all flows into one, but always update whenever data changes
     * Cons: could be heavier to when mapping
     */
    private fun CoroutineScope.observeAtfComponentFlow() {
        launch {
            val allFlows: List<Flow<Any?>> = listOf(dynamicPositionRepository.flow) + atfFlows
            Log.d("atfflow", "observeAtfComponentFlow: ${atfFlows.joinToString(",") { it.toString() }}")
            combine(allFlows) { list ->
                val dynamicPos = list[0] as? AtfDataList
                val listAtfData = list.drop(1) as List<AtfData?>
                Log.d(
                    "atfflow",
                    "7. HomeAtfUseCase : observeAtfComponentFlow: dynamic pos $dynamicPos\n" +
                        "${listAtfData.joinToString("\n"){ it.toString() }}"
                )
                if (dynamicPos != null && dynamicPos.status == AtfDataList.STATUS_SUCCESS && dynamicPos.listAtfData.isNotEmpty() && !dynamicPos.isCache) {
                    val latest = dynamicPos.updateAtfContents(listAtfData)
                    launch { emitAndSave(latest) }
                    Log.d("atfflow", "7. HomeAtfUseCase : observeAtfComponentFlow: emit $latest")
                }
            }.launchIn(this)
        }
    }

    /**
     * Fetch data for each ATF components
     */
    private suspend fun getEachAtfComponentData(value: AtfDataList) {
        Log.d("atfflow", "6. HomeAtfUseCase - getEachAtfComponentData: ")
        coroutineScope {
            launch {
                value.listAtfData.forEach { data ->
                    val metadata = data.atfMetadata
                    when (metadata.component) {
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

//    /**
//     * Exploration of other alternatives
//     */
//    private fun CoroutineScope.combineAtfFlows() {
//        val allFlows: List<StateFlow<Any?>> = dynamicPositionFlows + atfFlows
//        combine(allFlows) { list ->
//            val cachedDynamicPosition = list[0] as? AtfDataList
//            val remoteDynamicPosition = list[1] as? AtfDataList
//            val atfComponents = list.drop(2) as List<AtfData?>
//            val allComponentsReady = atfComponents.all { it?.atfContent != null }
//            Log.d("atfflow", "5. HomeAtfUseCase - combineAtfFlows. all ready: $allComponentsReady")
//
//            val currentAtfList = flow.value
//            var newDynamicPosition: AtfDataList? = null
//
//            // if both cache and remote are ready
//            if(cachedDynamicPosition != null && remoteDynamicPosition != null) {
//                newDynamicPosition = if(allComponentsReady) {
//                    Log.d("atfflow", "5. HomeAtfUseCase - combineAtfFlows. cache and remote ready, all components ready")
//                    remoteDynamicPosition
//                } else {
//                    Log.d("atfflow", "5. HomeAtfUseCase - combineAtfFlows. cache and remote ready, not all components ready")
//                    cachedDynamicPosition
//                }
//            } else {
//                // if cache comes earlier than remote
//                if(cachedDynamicPosition != null && remoteDynamicPosition == null) {
//                    Log.d("atfflow", "5. HomeAtfUseCase - combineAtfFlows. cache first")
//                    newDynamicPosition = cachedDynamicPosition
//                }
//                // if remote comes earlier than cache
//                else if(cachedDynamicPosition == null && remoteDynamicPosition != null) {
//                    /**
//                     * if all components are ready, then just emit remote data.
//                     * if current flow already has data, compare the position first, then:
//                     *  - if same position, then update isCache and isLatestData
//                     *  - if different position, then:
//                     *      - if remote data are all ready, update the whole list
//                     *      - else do nothing -> wait until all data ready
//                     */
//                    if(allComponentsReady) {
//                        Log.d("atfflow", "5. HomeAtfUseCase - combineAtfFlows. remote first. initial or all ready")
//                        newDynamicPosition = remoteDynamicPosition
//                    } else {
//                        currentAtfList ?: return@combine
//                        Log.d("atfflow", "5. HomeAtfUseCase - combineAtfFlows. remote first. not all ready")
//                        val isSamePosition = currentAtfList.positionEquals(remoteDynamicPosition.listAtfData)
//                        if(isSamePosition) {
//                            Log.d("atfflow", "5. HomeAtfUseCase - combineAtfFlows. remote first. not all ready but same position, so update isCache and isLatestData")
//                            newDynamicPosition = currentAtfList.copy(isCache = false)
//                        }
//                    }
//                }
//                val needToFetchComponents = !currentAtfList?.positionEquals(newDynamicPosition?.listAtfData).orFalse()
// //                if(needToFetchComponents) {
// //                    launch { getEachAtfComponentData(it) }
// //                }
//            }
//            newDynamicPosition?.let {
//                _flow.emit(newDynamicPosition)
// //                Log.d("atfflow", "5. HomeAtfUseCase - combineAtfFlows. RESULT: need to fetch all: $needToFetchComponents | new data: $newData")
//            }
//        }.launchIn(this)
//    }

//    private suspend fun updateSourceOnly(isCache: Boolean) {
//        val currentData = flow.value
//        _flow.emit(currentData?.copy(isCache = isCache, isLatestData = false))
//    }
//
//    private suspend fun updateDynamicPosition(dynamicPosition: AtfDataList) {
//        if(flow.value == null) {
//            _flow.emit(dynamicPosition)
//        } else {
//            //if from remote, update metadata and source but keep the cached content
//            if(!dynamicPosition.isCache) {
//                flow.value?.listAtfData?.let {
//                    val copiedContent = dynamicPosition.copyAtfContents(
//                        it.map { it.atfContent }
//                    )
//                    _flow.emit(copiedContent)
//                }
//            } else {
//                _flow.emit(dynamicPosition)
//            }
//        }
//    }

//    private fun CoroutineScope.observeDynamicPositionFlow() {
//        launch {
//            dynamicPositionRepository.cacheFlow.collect { value ->
//                Log.d("atfflow", "5. HomeAtfUseCase : observeDynamicPositionFlow $value")
//                if(value != null) {
//                    //if dynamic position remains the same, only update the source
//                    if(flow.value?.positionEquals(value.listAtfData) == true) {
//                        launch { updateSourceOnly(value.isCache) }
//                    } else {
//                        //if returns different positions, update the whole position
//                        //and fetch data for each
//                        launch { updateDynamicPosition(value) }
// //                        launch { getEachAtfComponentData(value) }
//                    }
//                }
//            }
//        }
//    }

//    private fun CoroutineScope.observeAtfComponentFlow() {
//        atfFlows.forEachIndexed { idx, stateFlow ->
//            launch {
//                flow.combine(stateFlow) { dynamicPos, atfData ->
//                    Log.d("atfflow", "6. HomeAtfUseCase : observeAtfComponentFlow ${atfFlowListName[idx]} $atfData")
//                    // updating flow with remote atf data should be done after
//                    // dynamic position coming from remote to avoid multiple updating data
//                    if(dynamicPos != null && !dynamicPos.isCache && atfData != null) {
//                        dynamicPos.let { currentAtf ->
//                            //first layer should check if dynamic position already from remote
//                            val model = currentAtf.listAtfData.find { it.atfMetadata == atfData.atfMetadata } ?: return@combine
//                            val index = currentAtf.listAtfData.indexOf(model)
//                            val newList = currentAtf.listAtfData.toMutableList().apply {
//                                set(index, model.copy(atfContent = atfData.atfContent))
//                            }
//                            val newModel = currentAtf.copy(listAtfData = newList)
//                            _flow.emit(newModel)
//                            Log.d("atfflow", "6. HomeAtfUseCase : observeAtfComponentFlow ${atfFlowListName[idx]} emit $flow with $newModel")
//                        }
//                    }
//                }.launchIn(CoroutineScope(homeDispatcher.io))
//            }
//        }
//
// //        launch {
// //            val allFlows: List<StateFlow<Any?>> = listOf(flow) + atfFlows
// //            combine(allFlows) { list ->
// //                val dynamicPos = list[0] as? AtfDataList
// //                val listAtfData = list.drop(1) as List<AtfData?>
// //                val hasAllAtfComplete = listAtfData.all { it == null || it.atfContent != null }
// //                if(dynamicPos != null && !dynamicPos.isCache && hasAllAtfComplete) {
// //                    dynamicPos.let { currentAtf ->
// //                        val newList = currentAtf.listAtfData.toMutableList()
// //                        listAtfData.forEach { atfData ->
// //                            val model = currentAtf.listAtfData.find { it.atfMetadata == atfData?.atfMetadata } ?: return@combine
// //                            val index = currentAtf.listAtfData.indexOf(model)
// //                            newList[index] = model.copy(atfContent = atfData?.atfContent)
// //                        }
// //                        val newModel = currentAtf.copy(listAtfData = newList)
// //                        _flow.emit(newModel)
// //                    }
// //                }
// //            }.launchIn(this)
// //        }
//    }
//
//    private fun CoroutineScope.combineAtfFlows() {
//        val allFlows: List<StateFlow<Any?>> = dynamicPositionFlows + atfFlows
//        combine(allFlows) { list ->
//            val cachedDynamicPosition = list[0] as? AtfDataList
//            val remoteDynamicPosition = list[1] as? AtfDataList
//            val atfComponents = list.drop(2) as List<AtfData?>
//            val allComponentsReady = atfComponents.all { it?.atfContent != null }
//
//            val currentAtfList = flow.value
//            var newDynamicPosition: AtfDataList? = null
//
//            // if cache comes earlier than remote
//            if(cachedDynamicPosition != null && remoteDynamicPosition == null) {
//                newDynamicPosition = cachedDynamicPosition
//            }
//            // if remote comes earlier than cache
//            else if(remoteDynamicPosition != null && cachedDynamicPosition == null) {
//                /**
//                 * if current flow is still null (initial), then just emit remote data.
//                 * if current flow already has data, compare the position first, then:
//                 *  - if same position, then update isCache and isLatestData
//                 *  - if different position, then:
//                 *      - if remote data are all ready, update the whole list
//                 *      - else do nothing -> wait until all data ready
//                 */
//                if(currentAtfList == null || allComponentsReady) {
//                    newDynamicPosition = remoteDynamicPosition
//                } else {
//                    val isSamePosition = currentAtfList.positionEquals(remoteDynamicPosition.listAtfData)
//                    if(isSamePosition) {
//                        newDynamicPosition = currentAtfList.copy(
//                            isCache = false,
//                            isLatestData = true
//                        )
//                    }
//                }
//            }
//            // if both cache and remote are ready
//            else {
//                newDynamicPosition = if(allComponentsReady) {
//                    remoteDynamicPosition
//                } else {
//                    cachedDynamicPosition
//                }
//            }
//
//            newDynamicPosition?.let {
//                _flow.emit(newDynamicPosition.updateAtfContents(atfComponents))
//                if(!it.isLatestData) {
//                    Log.d("atfflow", "combineAtfFlows: isLatestData ${it.isLatestData}")
//                    launch { getEachAtfComponentData(it) }
//                }
//            }
//
// //            flow.value?.let { currentAtfList ->
// //                if(newDynamicPosition != null) {
// //                    // if remote comes earlier than cache
// //                    if(!currentAtfList.isCache && newDynamicPosition.isCache) {
// //                        currentAtfList
// //                    } else {
// //                        val isSamePosition = currentAtfList.positionEquals(newDynamicPosition.listAtfData)
// //                        // if remote has the same position as cache,
// //                        // then set isDifferentPosition to false to avoid re-fetching data for each atf components
// //                        // if remote has different position as cache,
// //                        // set isDifferentPosition to true to re-fetch data for each atf components
// //                        newAtfList = if(isSamePosition) {
// //                            currentAtfList.copy(
// //                                isCache = false,
// //                                isLatestData = true,
// //                            )
// //                        } else {
// //                            currentAtfList.updateMetaData(
// //                                isCache = false,
// //                                isLatestData = false,
// //                                listAtfMetadata = newDynamicPosition.listAtfData.map { it.atfMetadata },
// //                            )
// //                        }
// //                        _flow.emit(newAtfList)
// //                        return@combine
// //                    }
// //                }
// //            }
//
// //            if(value != null) {
// //                //if dynamic position remains the same, only update the source
// //                if(flow.value?.hasSamePosition(value.listAtfData) == true) {
// //                    launch { updateSourceOnly(value.isCache) }
// //                } else {
// //                    //if returns different positions, update the whole position
// //                    //and fetch data for each
// //                    launch { updateDynamicPosition(value) }
// ////                        launch { getEachAtfComponentData(value) }
// //                }
// //            }
//
//
// //            val hasAllAtfComplete = listAtfData.all { it == null || it.atfContent != null }
// //            if(dynamicPos != null && !dynamicPos.isCache && hasAllAtfComplete) {
// //                dynamicPos.let { currentAtf ->
// //                    val newList = currentAtf.listAtfData.toMutableList()
// //                    listAtfData.forEach { atfData ->
// //                        val model = currentAtf.listAtfData.find { it.atfMetadata == atfData?.atfMetadata } ?: return@combine
// //                        val index = currentAtf.listAtfData.indexOf(model)
// //                        newList[index] = model.copy(atfContent = atfData?.atfContent)
// //                    }
// //                    val newModel = currentAtf.copy(listAtfData = newList)
// //                    _flow.emit(newModel)
// //                }
// //            }
//        }.launchIn(this)
//    }
//
//    private fun getUpdatedAtfList(
//        currentAtfList: AtfDataList,
//        newAtfData: AtfData,
//    ): AtfDataList {
//        val model = currentAtfList.listAtfData.find { it.atfMetadata == newAtfData.atfMetadata } ?: return currentAtfList
//        val index = currentAtfList.listAtfData.indexOf(model)
//        val newList = currentAtfList.listAtfData.toMutableList().apply {
//            set(index, model.copy(atfContent = newAtfData.atfContent))
//        }
//        val newModel = currentAtfList.copy(listAtfData = newList)
//        return newModel
//    }
}
