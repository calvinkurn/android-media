package com.tokopedia.videoTabComponent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.ContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlot
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetFeedReminderInfoData
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams
import com.tokopedia.videoTabComponent.domain.usecase.GetPlayContentUseCase
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlayFeedVideoTabViewModel@Inject constructor(
        private val baseDispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val getPlayContentUseCase: GetPlayContentUseCase,
        private val lazyReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
        private val playWidgetTools: PlayWidgetTools,
): BaseViewModel(baseDispatcher.main){

    companion object {
        private const val DEFAULT_GROUP_VALUE = "feeds_channels"
        private const val DEFAULT_LIVE_GROUP_VALUE = "feeds_channels_live"
        private const val DEFAULT_UPCOMING_GROUP_VALUE = "feeds_channels_upco"
        private const val WIDGET_LIVE ="live"
        private const val WIDGET_UPCOMING ="upcoming"
    }

     var currentCursor = ""
     var currentLivePageCursor = ""

     var currentSourceType = ""
     var currentSourceId = ""
    private var currentGroup = DEFAULT_GROUP_VALUE
    private var currentGroupSeeMorePage = DEFAULT_LIVE_GROUP_VALUE
    val getPlayInitialDataRsp = MutableLiveData<Result<ContentSlotResponse>>()
    val getPlayDataRsp = MutableLiveData<Result<ContentSlotResponse>>()
    val getLivePlayDataRsp = MutableLiveData<Result<ContentSlotResponse>>()
    val getPlayDataForSlotRsp = MutableLiveData<Result<ContentSlotResponse>>()
    private val playWidgetUIMutableLiveData: MutableLiveData<PlayWidgetState?> = MutableLiveData(PlayWidgetState(isLoading = true))
    private val _reminderObservable = MutableLiveData<Result<PlayWidgetFeedReminderInfoData>>()


    private var reminderData: Pair<String, PlayWidgetReminderType>? = null



    private val reminderUseCase: PlayWidgetReminderUseCase
        get() = lazyReminderUseCase.get()
    val reminderObservable: LiveData<Result<PlayWidgetFeedReminderInfoData>>
        get() = _reminderObservable



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
                getPlayDataForSlotRsp.value = Success(results)

            } else {
                getPlayDataRsp.value = Success(results)
            }

        }) {
            getPlayDataRsp.value = Fail(it)
        }
    }
    fun getLivePlayData(widgetType: String, sourceId: String = "", sourceType: String) {
        if (widgetType == WIDGET_LIVE)
            currentGroupSeeMorePage = DEFAULT_LIVE_GROUP_VALUE
        else if (widgetType == WIDGET_UPCOMING)
            currentGroupSeeMorePage = DEFAULT_UPCOMING_GROUP_VALUE


        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                getLivePlayPageDataResult(sourceId, sourceType)
            }
            currentLivePageCursor = results.playGetContentSlot.meta.next_cursor

            getLivePlayDataRsp.value = Success(results)

        }) {
            getLivePlayDataRsp.value = Fail(it)
        }

    }
    private suspend fun updateToggleReminder(channelId: String,
                                     reminderType: PlayWidgetReminderType,
                                     coroutineContext: CoroutineContext = Dispatchers.IO): PlayWidgetReminder {
        return withContext(coroutineContext) {
            reminderUseCase.setRequestParams(PlayWidgetReminderUseCase.createParams(channelId, reminderType.reminded))
            reminderUseCase.executeOnBackground()
        }
    }

     fun updatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType, position: Int) {
        reminderData = null
        updateWidget {
            playWidgetTools.updateActionReminder(it, channelId, reminderType)
        }

        launchCatchError(block = {
            val response = updateToggleReminder(
                    channelId,
                    reminderType
            )


            when (val success = playWidgetTools.mapWidgetToggleReminder(response)) {
                success -> {
                    val playWidgetFeedReminderInfoData = PlayWidgetFeedReminderInfoData(channelId = channelId,reminderType = reminderType, itemPosition = position)
                    _reminderObservable.postValue(Success(playWidgetFeedReminderInfoData))
                }
                else -> {
                    updateWidget {
                        playWidgetTools.updateActionReminder(it, channelId, reminderType.switch())
                    }
                    _reminderObservable.postValue(Fail(Throwable()))
                }
            }
        }) { throwable ->
            updateWidget {
                playWidgetTools.updateActionReminder(it, channelId, reminderType.switch())
            }
            _reminderObservable.postValue(Fail(throwable))
        }
    }


    private fun updateWidget(onUpdate: (oldVal: PlayWidgetState) -> PlayWidgetState) {
        playWidgetUIMutableLiveData.value?.let { currentValue ->
            playWidgetUIMutableLiveData.postValue(onUpdate(currentValue))
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
    private suspend fun getLivePlayPageDataResult(sourceId: String, sourceType: String): ContentSlotResponse {
        try {
            return getPlayContentUseCase.execute(VideoPageParams(cursor = currentLivePageCursor, sourceId = sourceId, sourceType = sourceType, group = currentGroupSeeMorePage))
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }


}