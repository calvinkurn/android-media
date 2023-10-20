package com.tokopedia.play.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.PLAY_KEY_PAGE_SOURCE_NAME
import com.tokopedia.play.PLAY_KEY_SOURCE_ID
import com.tokopedia.play.PLAY_KEY_SOURCE_TYPE
import com.tokopedia.play.PLAY_KEY_WIDGET_ID
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.storage.PlayQueryParamStorage
import com.tokopedia.play.view.type.PlaySource
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play_common.model.result.PageInfo
import com.tokopedia.play_common.model.result.PageResult
import com.tokopedia.play_common.model.result.PageResultState
import com.tokopedia.play_common.model.ui.ArchivedUiModel
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by jegul on 19/01/21
 */
class PlayParentViewModel @AssistedInject constructor(
    @Assisted private val handle: SavedStateHandle,
    private val playChannelStateStorage: PlayChannelStateStorage,
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val repo: PlayViewerRepository,
    private val queryParamStorage: PlayQueryParamStorage,
    private val preference: PlayPreference,
    private val analytic: PlayAnalytic,
    pageMonitoring: PlayPltPerformanceCallback
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(handle: SavedStateHandle): PlayParentViewModel
    }

    val userId: String
        get() = userSession.userId

    /**
     * LiveData
     */
    val observableChannelIdsResult: LiveData<PageResult<List<PlayChannelData>>>
        get() = _observableChannelIdsResult
    private val _observableChannelIdsResult = MutableLiveData<PageResult<List<PlayChannelData>>>()

    val observableFirstChannelEvent: LiveData<Event<Unit>>
        get() = _observableFirstChannelEvent
    private val _observableFirstChannelEvent = MutableLiveData<Event<Unit>>()

    val observableOnBoarding: LiveData<Event<Unit>>
        get() = _observableOnBoarding
    private val _observableOnBoarding = MutableLiveData<Event<Unit>>()

    val source: PlaySource
        get() = PlaySource(
            type = handle[PLAY_KEY_SOURCE_TYPE] ?: "",
            id = handle[PLAY_KEY_SOURCE_ID] ?: ""
        )

    private val startingChannelId: String?
        get() = handle[PLAY_KEY_CHANNEL_ID]

    private val pageSourceName: String
        get() = handle[PLAY_KEY_PAGE_SOURCE_NAME] ?: ""

    private val widgetId: String
        get() = handle[PLAY_KEY_WIDGET_ID] ?: ""

    private val mVideoStartMillis: String?
        get() = handle[KEY_START_TIME]

    private val shouldTrack: String?
        get() = handle[KEY_SHOULD_TRACK]

    private var mNextKey: GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey = getNextChannelIdKey(
        channelId = startingChannelId ?: error("Channel ID must be provided"),
        source = source
    )

    init {
        queryParamStorage.pageSourceName = pageSourceName.ifEmpty { source.type }
        queryParamStorage.widgetId = widgetId

        pageMonitoring.startNetworkRequestPerformanceMonitoring()
        loadNextPage()
    }

    fun setNewChannelParams(bundle: Bundle) {
        val channelId = bundle.get(PLAY_KEY_CHANNEL_ID) as? String

        val isFromPiP = bundle.getBoolean(IS_FROM_PIP, false)

        if (!isFromPiP && !channelId.isNullOrEmpty()) {
            val sourceType: String = bundle.getString(PLAY_KEY_SOURCE_TYPE, "")
            val widgetId = bundle.getString(PLAY_KEY_WIDGET_ID, "")
            val pageSourceName = bundle.getString(PLAY_KEY_PAGE_SOURCE_NAME, "")

            handle.set(PLAY_KEY_CHANNEL_ID, channelId)
            handle.set(PLAY_KEY_SOURCE_TYPE, sourceType)
            handle.set(PLAY_KEY_SOURCE_ID, bundle.get(PLAY_KEY_SOURCE_ID))
            handle.set(PLAY_KEY_PAGE_SOURCE_NAME, pageSourceName)
            handle.set(PLAY_KEY_WIDGET_ID, widgetId)
            handle.set(KEY_START_TIME, bundle.get(KEY_START_TIME))
            handle.set(KEY_SHOULD_TRACK, bundle.get(KEY_SHOULD_TRACK))

            queryParamStorage.pageSourceName = pageSourceName.ifEmpty { sourceType }
            queryParamStorage.widgetId = widgetId

            mNextKey = getNextChannelIdKey(channelId, source)
            loadNextPage()
        }
    }

    fun getLatestChannelStorageData(channelId: String): PlayChannelData = playChannelStateStorage.getData(channelId) ?: error("Channel with ID $channelId not found")

    fun setLatestChannelStorageData(
        channelId: String,
        data: PlayChannelData
    ) {
        if (playChannelStateStorage.getData(channelId) != null) playChannelStateStorage.setData(channelId, data)
    }

    fun loadNextPage() {
        val nextKey = mNextKey
        if (nextKey is GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey.Cursor && nextKey.cursor.isEmpty()) return

        getChannelDetailsWithRecom(nextKey)
    }

    private fun getChannelDetailsWithRecom(nextKey: GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey) {
        val isFirstPage = nextKey is GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey.ChannelId
        if (isFirstPage) {
            _observableFirstChannelEvent.value = Event(Unit)
            playChannelStateStorage.clearData()
        }

        _observableChannelIdsResult.value = PageResult.Loading(playChannelStateStorage.getChannelList())

        viewModelScope.launchCatchError(block = {
            withContext(dispatchers.io) {
                val response = repo.getChannels(
                    nextKey,
                    PlayChannelDetailsWithRecomMapper.ExtraParams(
                        channelId = startingChannelId,
                        videoStartMillis = mVideoStartMillis?.toLong() ?: 0,
                        shouldTrack = shouldTrack?.toBoolean() ?: true,
                        sourceType = source.type
                    )
                )

                mNextKey = GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey.Cursor(response.cursor)

                response.channelList.forEach {
                    playChannelStateStorage.setData(it.id, it)
                }
            }

            startingChannelId?.let { channelId ->
                _observableChannelIdsResult.value = PageResult(
                    currentValue = playChannelStateStorage.getChannelList(),
                    state = when {
                        playChannelStateStorage.getData(channelId)?.upcomingInfo?.isUpcoming == true -> PageResultState.Upcoming(channelId = channelId)
                        playChannelStateStorage.getData(channelId)?.status?.channelStatus?.statusType?.isArchive == true -> PageResultState.Archived(playChannelStateStorage.getData(channelId)?.status?.config?.archivedModel ?: ArchivedUiModel.Empty)
                        else -> PageResultState.Success(pageInfo = PageInfo.Unknown, isFirstPage)
                    }
                )
            } ?: run {
                _observableChannelIdsResult.value = PageResult(
                    currentValue = playChannelStateStorage.getChannelList(),
                    state = PageResultState.Success(pageInfo = PageInfo.Unknown, isFirstPage)
                )
            }
        }, onError = {
                _observableChannelIdsResult.value = PageResult(
                    currentValue = playChannelStateStorage.getChannelList(),
                    state = PageResultState.Fail(it)
                )
            })
    }

    private fun getNextChannelIdKey(channelId: String, source: PlaySource) = GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey.ChannelId(
        channelId = channelId,
        source = source
    )

    fun refreshChannel() {
        startingChannelId?.let {
            mNextKey = getNextChannelIdKey(it, source)
            loadNextPage()
        }
    }

    fun setupOnBoarding(isFirstPage: Boolean) {
        val hasBeenShown = preference.isOnBoardingHasBeenShown()

        if (!isFirstPage) return

        val currentValue = _observableChannelIdsResult.value?.currentValue ?: return
        if (currentValue.isEmpty()) return
        val firstChannel = currentValue.first()

        analytic.openScreenWithOnBoarding(
            firstChannel.id,
            firstChannel.channelDetail.channelInfo.channelType,
            !hasBeenShown
        )

        if (hasBeenShown) return

        viewModelScope.launch {
            withContext(dispatchers.main) {
                _observableOnBoarding.value = Event(Unit)
                preference.setOnBoardingHasShown()
            }
        }
    }

    companion object {
        private const val KEY_START_TIME = "start_time"
        private const val IS_FROM_PIP = "is_from_pip"
        private const val KEY_SHOULD_TRACK = "should_track"
    }
}
