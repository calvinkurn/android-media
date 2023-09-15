package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.beranda.data.newatf.banner.BannerRepository
import com.tokopedia.home.beranda.data.newatf.position.DynamicPositionRepository
import com.tokopedia.home.constant.AtfKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class AtfRepository @Inject constructor(
    private val dynamicPositionRepository: DynamicPositionRepository,
    private val bannerRepository: BannerRepository,
) {
    private val flow: StateFlow<AtfDataList?>
        get() = _flow
    private var _flow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)

    init {
        observeAtfFlow(bannerRepository.flow)
    }

    suspend fun getDynamicPosition() {
        dynamicPositionRepository.flow.collect { value ->
            if(value == null) {
                //only fetch dynamic position on first load
                dynamicPositionRepository.getData()
            } else {
                //if dynamic position remains the same, only update the source
                if(flow.value?.hasSamePosition(value.listAtfData) == true) {
                    updateSourceOnly(value.isCache)
                } else {
                    //if returns different positions, update the whole position
                    //and fetch data for each
                    updateDynamicPosition(value)
                    getDataForEach(value)
                }
//                if((flow.value == null && value.isCache) ||
//                    (!value.isCache && flow.value != value)) {
//                    updateDynamicPosition(value)
//                    //eligible for getting data for each widget if:
//                    //1. first load dynamic position from cache
//                    //2. load dynamic position from remote with different value from existing
//                    getDataForEach(value)
//                }
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
                    _flow.emit(
                        dynamicPosition.copyAtfContents(
                            dynamicPosition.listAtfData.map { it.atfContent }
                        )
                    )
                }
            } else {
                _flow.emit(dynamicPosition)
            }
        }
    }

    private suspend fun getDataForEach(value: AtfDataList) {
        value.listAtfData.forEach { data ->
            val metadata = data.atfMetadata
            when(metadata.component) {
                AtfKey.TYPE_BANNER -> bannerRepository.getData(metadata)
            }
        }
    }

    private fun observeAtfFlow(atfFlow: StateFlow<AtfData?>) {
        flow.combine(atfFlow) { dynamicPos, atfData ->
            // updating flow with remote atf data should be done after
            // dynamic position coming from remote to avoid multiple updating data
            if(dynamicPos != null && !dynamicPos.isCache) {
                flow.value?.let { currentAtf ->
                    //first layer should check if dynamic position already from remote
                    val model = currentAtf.listAtfData.find { it.atfMetadata == atfData?.atfMetadata } ?: return@combine
                    val index = currentAtf.listAtfData.indexOf(model)
                    val newList = currentAtf.listAtfData.toMutableList().apply {
                        set(index, model.copy(atfContent = atfData?.atfContent))
                    }
                    val newModel = currentAtf.copy(listAtfData = newList)
                    _flow.emit(newModel)
                }
            }
        }
    }
}
