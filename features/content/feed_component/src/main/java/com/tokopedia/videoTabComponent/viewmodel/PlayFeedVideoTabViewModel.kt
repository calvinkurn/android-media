package com.tokopedia.videoTabComponent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.domain.PlayVideoTabRepository
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.ContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotItems
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetFeedReminderInfoData
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams
import com.tokopedia.videoTabComponent.view.uimodel.SelectedPlayWidgetCard
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class PlayFeedVideoTabViewModel@Inject constructor(
    private val baseDispatcher: CoroutineDispatchers,
    private val repository: PlayVideoTabRepository,
    private val playWidgetTools: PlayWidgetTools,
    private val userSession: UserSessionInterface
) : BaseViewModel(baseDispatcher.main) {

    companion object {
        private const val DEFAULT_GROUP_VALUE = "feeds_channels"
        private const val DEFAULT_LIVE_GROUP_VALUE = "feeds_channels_live"
        private const val DEFAULT_UPCOMING_GROUP_VALUE = "feeds_channels_upco"
        private const val WIDGET_LIVE = "live"
        private const val WIDGET_UPCOMING = "upcoming"
    }

    var currentCursor = ""
    var currentLivePageCursor = ""

    var currentSourceType = ""
    var currentSourceId = ""

    private var currentGroup = DEFAULT_GROUP_VALUE
    private var currentGroupSeeMorePage = DEFAULT_LIVE_GROUP_VALUE
    private val _getPlayInitialDataRsp = MutableLiveData<Result<ContentSlotResponse>>()
    private val _getPlayDataRsp = MutableLiveData<Result<ContentSlotResponse>>()
    private val _getPlayDataForSlotRsp = MutableLiveData<Result<ContentSlotResponse>>()
    private val _getLiveOrUpcomingPlayDataRsp = MutableLiveData<Result<ContentSlotResponse>>()
    private val _reminderObservable = MutableLiveData<Result<PlayWidgetFeedReminderInfoData>>()
    private val _playWidgetReminderEvent = MutableLiveData<PlayWidgetFeedReminderInfoData>()

    val getPlayInitialDataRsp: LiveData<Result<ContentSlotResponse>>
        get() = _getPlayInitialDataRsp

    val getPlayDataRsp: LiveData<Result<ContentSlotResponse>>
        get() = _getPlayDataRsp

    val getPlayDataForSlotRsp: LiveData<Result<ContentSlotResponse>>
        get() = _getPlayDataForSlotRsp

    val getLiveOrUpcomingPlayDataRsp: LiveData<Result<ContentSlotResponse>>
        get() = _getLiveOrUpcomingPlayDataRsp

    val playWidgetReminderEvent: LiveData<PlayWidgetFeedReminderInfoData>
        get() = _playWidgetReminderEvent

    val reminderObservable: LiveData<Result<PlayWidgetFeedReminderInfoData>>
        get() = _reminderObservable

    private val _selectedPlayWidgetCard = MutableLiveData<SelectedPlayWidgetCard>()
    var selectedPlayWidgetCard: SelectedPlayWidgetCard
        get() = _selectedPlayWidgetCard.value ?: SelectedPlayWidgetCard.Empty
        set(value) {
            _selectedPlayWidgetCard.value = value
        }

    private val _getSelectedTabDefaultPosition = MutableLiveData<Int>()
    var selectedTabDefaultPosition: Int
        get() = _getSelectedTabDefaultPosition.value ?: 0
        set(value) {
            _getSelectedTabDefaultPosition.value = value
        }

    fun setDefaultValuesOnRefresh() {
        currentCursor = ""
        currentGroup = DEFAULT_GROUP_VALUE
        currentSourceId = ""
        currentSourceType = ""
    }

    fun getInitialPlayData(selectedChipValue: String = "") {
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                repository.getPlayData(VideoPageParams(cursor = currentCursor, sourceId = currentSourceId, sourceType = currentSourceType, group = currentGroup))
            }
            val tabData = FeedPlayVideoTabMapper.getTabData(results.playGetContentSlot)
            if (tabData.isNotEmpty()) {
                tabData.first().let {
                    val tabList = it.items
                    tabList.let {
                        if (tabList.isNotEmpty()) {
                            val firstListItem = tabList.first()
                            var selectedTabItem: PlaySlotItems? = null
                            tabList.mapIndexed { index, playSlotItems ->
                                if (playSlotItems.slug_id == selectedChipValue) {
                                    _getSelectedTabDefaultPosition.value = index
                                    selectedTabItem = playSlotItems
                                }
                            }
                            val tabItem = selectedTabItem ?: firstListItem
                            currentSourceId = tabItem.source_id
                            currentGroup = tabItem.group
                            currentSourceType = tabItem.source_type
                        }
                    }
                }
            }
            getPlayData(false, null)
            _getPlayInitialDataRsp.value = Success(results)
        }) {
            _getPlayInitialDataRsp.value = Fail(it)
        }
    }

    fun getPlayData(isClickFromTabMenu: Boolean, videoPageParams: VideoPageParams?) {
        if (isClickFromTabMenu) {
            videoPageParams?.let {
                currentCursor = videoPageParams.cursor
                currentSourceId = videoPageParams.sourceId
                currentGroup = videoPageParams.group
                currentSourceType = videoPageParams.sourceType
            }
        }
        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                videoPageParams?.let { repository.getPlayData(videoPageParams) }
                    ?: repository.getPlayData(
                        VideoPageParams(
                            cursor = currentCursor,
                            sourceId = currentSourceId,
                            sourceType = currentSourceType,
                            group = currentGroup
                        )
                    )
            }
            currentCursor = results.playGetContentSlot.meta.next_cursor
            if (isClickFromTabMenu) {
                _getPlayDataForSlotRsp.value = Success(results)
            } else {
                _getPlayDataRsp.value = Success(results)
            }
        }) {
            if (isClickFromTabMenu) {
                _getPlayDataForSlotRsp.value = Fail(it)
            } else {
                _getPlayDataRsp.value = Fail(it)
            }
        }
    }

    fun getPlayDetailPageData(widgetType: String, sourceId: String = "", sourceType: String) {
        if (widgetType == WIDGET_LIVE) {
            currentGroupSeeMorePage = DEFAULT_LIVE_GROUP_VALUE
        } else if (widgetType == WIDGET_UPCOMING) {
            currentGroupSeeMorePage = DEFAULT_UPCOMING_GROUP_VALUE
        }

        launchCatchError(block = {
            val results = withContext(baseDispatcher.io) {
                repository.getPlayDetailPageResult(
                    cursor = currentLivePageCursor,
                    sourceId = sourceId,
                    sourceType = sourceType,
                    group = currentGroupSeeMorePage
                )
            }
            currentLivePageCursor = results.playGetContentSlot.meta.next_cursor

            _getLiveOrUpcomingPlayDataRsp.value = Success(results)
        }) {
            _getLiveOrUpcomingPlayDataRsp.value = Fail(it)
        }
    }

    fun updatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType, position: Int, isLoggedIn: Boolean = userSession.isLoggedIn) {
        if (!isLoggedIn) {
            _playWidgetReminderEvent.value =
                PlayWidgetFeedReminderInfoData(
                    channelId = channelId,
                    reminderType = reminderType,
                    itemPosition = position
                )
        } else {
            launchCatchError(block = {
                val response = repository.updateToggleReminder(
                    channelId,
                    reminderType
                )

                if (playWidgetTools.mapWidgetToggleReminder(response)) {
                    val playWidgetFeedReminderInfoData = PlayWidgetFeedReminderInfoData(
                        channelId = channelId,
                        reminderType = reminderType,
                        itemPosition = position
                    )
                    _reminderObservable.postValue(Success(playWidgetFeedReminderInfoData))
                } else {
                    if (reminderType == PlayWidgetReminderType.Reminded) {
                        _reminderObservable.postValue(Fail(CustomUiMessageThrowable(com.tokopedia.content.common.R.string.feed_video_tab_failed_to_set_reminder_text)))
                    } else {
                        _reminderObservable.postValue(Fail(CustomUiMessageThrowable(com.tokopedia.content.common.R.string.feed_video_tab_failed_to_unset_reminder_text)))
                    }
                }
            }) { throwable ->
                when (throwable) {
                    is UnknownHostException, is SocketTimeoutException, is ConnectException -> {
                        if (reminderType == PlayWidgetReminderType.Reminded) {
                            _reminderObservable.postValue(Fail(CustomUiMessageThrowable(com.tokopedia.content.common.R.string.feed_video_tab_failed_to_set_reminder_text)))
                        } else {
                            _reminderObservable.postValue(Fail(CustomUiMessageThrowable(com.tokopedia.content.common.R.string.feed_video_tab_failed_to_unset_reminder_text)))
                        }
                    }
                    else -> {
                        _reminderObservable.postValue(Fail(throwable))
                    }
                }
            }
        }
    }
}
