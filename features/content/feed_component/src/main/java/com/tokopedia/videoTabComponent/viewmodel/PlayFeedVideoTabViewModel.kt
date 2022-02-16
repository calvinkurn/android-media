package com.tokopedia.videoTabComponent.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.ContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlot
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams
import com.tokopedia.videoTabComponent.domain.usecase.GetPlayContentUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayFeedVideoTabViewModel@Inject constructor(
        private val baseDispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val getPlayContentUseCase: GetPlayContentUseCase
): BaseViewModel(baseDispatcher.main){

    companion object {
        private const val DEFAULT_GROUP_VALUE = "feeds_channels"
        private const val DEFAULT_LIVE_GROUP_VALUE = "feeds_channels_live"
    }

     var currentCursor = ""
     var currentLivePageCursor = ""

    private var currentSourceType = ""
    private var currentSourceId = ""
    private var currentGroup = DEFAULT_GROUP_VALUE
    val getPlayInitialDataRsp = MutableLiveData<Result<ContentSlotResponse>>()
    val getPlayDataRsp = MutableLiveData<Result<ContentSlotResponse>>()
    val getLivePlayDataRsp = MutableLiveData<Result<ContentSlotResponse>>()


    fun getInitialPlayData(){
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                getPlayDataResult()
            }
            val tabData = FeedPlayVideoTabMapper.getTabData(results.playGetContentSlot)
            if (tabData.isNotEmpty()){
                tabData.first().let {
                    val tabList = it.items
                    if (tabList.isNotEmpty()){
                        tabList.let {
                            val firstListItem = tabList.first()
                            currentSourceId = firstListItem.source_id
                            currentGroup = firstListItem.group
                            currentSourceType = firstListItem.source_type
                        }
                    }
                }
            }
            getPlayData(false, null)
            getPlayInitialDataRsp.value = Success(results)

        }) {
            getPlayInitialDataRsp.value = Fail(it)
        }
    }
    private fun getAppendedList(data: ContentSlotResponse) : List<PlaySlot>{
        val dataList = mutableListOf<PlaySlot>()
        if (getPlayInitialDataRsp.value != null && getPlayInitialDataRsp.value is Success) {
            val initialResponse = (getPlayInitialDataRsp.value as Success<ContentSlotResponse>).data
            dataList.addAll(initialResponse.playGetContentSlot.data)
        }
        if (getPlayDataRsp.value != null && getPlayDataRsp.value is Success) {
            val secondResponse = (getPlayDataRsp.value as Success<ContentSlotResponse>).data
            dataList.addAll(secondResponse.playGetContentSlot.data)
        }
        return dataList
    }

    fun getPlayData(isClickFromTabMenu: Boolean, videoPageParams: VideoPageParams?) {
        if (isClickFromTabMenu){
            videoPageParams?.let {
                currentCursor = videoPageParams.cursor
                currentSourceId = videoPageParams.sourceId
                currentGroup = videoPageParams.group
                currentSourceType = videoPageParams.sourceType
            }
        }
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                getPlayDataResult()
            }
            currentCursor = results.playGetContentSlot.meta.next_cursor
            if (isClickFromTabMenu) {
                var data = results
                val appendedList = getAppendedList(data)
                data.isDataFromTabClick = isClickFromTabMenu
                data.playGetContentSlot.appendeList = appendedList
                getPlayDataRsp.value = Success(data)
            } else {
                getPlayDataRsp.value = Success(results)
            }

        }) {
            getPlayDataRsp.value = Fail(it)
        }
    }
    fun getLivePlayData(){
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                getLivePlayPageDataResult()
            }
            currentLivePageCursor = results.playGetContentSlot.meta.next_cursor

            getLivePlayDataRsp.value = Success(results)

        }) {
            getLivePlayDataRsp.value = Fail(it)
        }

    }


    private suspend fun getPlayDataResult(): ContentSlotResponse {
        try {
            return getPlayContentUseCase.execute(VideoPageParams(cursor = currentCursor, sourceId = currentSourceId, sourceType = currentSourceType, group = currentGroup))
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }
    private suspend fun getLivePlayPageDataResult(): ContentSlotResponse {
        try {
            return getPlayContentUseCase.execute(VideoPageParams(cursor = currentLivePageCursor, sourceId = "", sourceType = "", group = DEFAULT_LIVE_GROUP_VALUE))
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }


}