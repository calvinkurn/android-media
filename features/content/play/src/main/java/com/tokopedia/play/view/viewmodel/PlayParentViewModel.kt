package com.tokopedia.play.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.PLAY_KEY_SOURCE_ID
import com.tokopedia.play.PLAY_KEY_SOURCE_TYPE
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.type.PlaySource
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play_common.model.result.PageInfo
import com.tokopedia.play_common.model.result.PageResult
import com.tokopedia.play_common.model.result.PageResultState
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 19/01/21
 */
class PlayParentViewModel constructor(
        private val handle: SavedStateHandle,
        private val playChannelStateStorage: PlayChannelStateStorage,
        private val getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase,
        private val playChannelMapper: PlayChannelDetailsWithRecomMapper,
        private val dispatchers: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        pageMonitoring: PlayPltPerformanceCallback,
) : ViewModel() {

    class Factory @Inject constructor(
            private val playChannelStateStorage: PlayChannelStateStorage,
            private val getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase,
            private val playChannelMapper: PlayChannelDetailsWithRecomMapper,
            private val dispatchers: CoroutineDispatchers,
            private val userSession: UserSessionInterface,
            private val pageMonitoring: PlayPltPerformanceCallback,
    ) {

        fun create(handle: SavedStateHandle): PlayParentViewModel {
            return PlayParentViewModel(
                    handle,
                    playChannelStateStorage,
                    getChannelDetailsWithRecomUseCase,
                    playChannelMapper,
                    dispatchers,
                    userSession,
                    pageMonitoring,
            )
        }
    }

    val userId: String
        get() = userSession.userId

    /**
     * LiveData
     */
    val observableChannelIdsResult: LiveData<PageResult<List<String>>>
        get() = _observableChannelIdsResult
    private val _observableChannelIdsResult = MutableLiveData<PageResult<List<String>>>()

    val observableFirstChannelEvent: LiveData<Event<Unit>>
        get() = _observableFirstChannelEvent
    private val _observableFirstChannelEvent = MutableLiveData<Event<Unit>>()

    val sourceType: String
        get() = handle[PLAY_KEY_SOURCE_TYPE] ?: ""
    
    val source: PlaySource
        get() = PlaySource.getBySource(
                sourceType = sourceType,
                sourceId = handle[PLAY_KEY_SOURCE_ID]
        )

    val startingChannelId: String?
        get() = handle[PLAY_KEY_CHANNEL_ID]

    private val mVideoStartMillis: String?
        get() = handle[KEY_START_TIME]

    private val shouldTrack: String?
        get() = handle[KEY_SHOULD_TRACK]

    private var mNextKey: GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey = getNextChannelIdKey(
            channelId = startingChannelId ?: error("Channel ID must be provided"),
            source = source
    )

    init {
        pageMonitoring.startNetworkRequestPerformanceMonitoring()
        loadNextPage()
    }

    fun setNewChannelParams(bundle: Bundle) {
        val channelId = bundle.get(PLAY_KEY_CHANNEL_ID) as? String

        val isFromPiP = bundle.getBoolean(IS_FROM_PIP, false)

        if (!isFromPiP && !channelId.isNullOrEmpty()) {
            handle.set(PLAY_KEY_CHANNEL_ID, channelId)
            handle.set(PLAY_KEY_SOURCE_TYPE, bundle.get(PLAY_KEY_SOURCE_TYPE))
            handle.set(PLAY_KEY_SOURCE_ID, bundle.get(PLAY_KEY_SOURCE_ID))
            handle.set(KEY_START_TIME, bundle.get(KEY_START_TIME))
            handle.set(KEY_SHOULD_TRACK, bundle.get(KEY_SHOULD_TRACK))

            mNextKey = getNextChannelIdKey(channelId, source)
            loadNextPage()
        }
    }

    fun getLatestChannelStorageData(channelId: String): PlayChannelData = playChannelStateStorage.getData(channelId) ?: error("Channel not found")

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
        if (nextKey is GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey.ChannelId) {
            _observableFirstChannelEvent.value = Event(Unit)
            playChannelStateStorage.clearData()
        }

        _observableChannelIdsResult.value = PageResult.Loading(playChannelStateStorage.getChannelList())

        viewModelScope.launchCatchError(block = {
            withContext(dispatchers.io) {
                val response = getChannelDetailsWithRecomUseCase.apply {
                    setRequestParams(GetChannelDetailsWithRecomUseCase.createParams(nextKey))
                }.executeOnBackground()

                mNextKey = GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey.Cursor(response.channelDetails.meta.cursor)

                playChannelMapper.map(response, PlayChannelDetailsWithRecomMapper.ExtraParams(
                        channelId = startingChannelId,
                        videoStartMillis = mVideoStartMillis?.toLong() ?: 0,
                        shouldTrack = shouldTrack?.toBoolean() ?: true,
                        sourceType = source.key
                    )
                ).forEach {
                    playChannelStateStorage.setData(it.id, it)
                }
            }

            _observableChannelIdsResult.value = PageResult(
                    currentValue = playChannelStateStorage.getChannelList(),
                    state = PageResultState.Success(pageInfo = PageInfo.Unknown)
            )
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

    companion object {
        private const val KEY_START_TIME = "start_time"
        private const val IS_FROM_PIP = "is_from_pip"
        private const val KEY_SHOULD_TRACK = "should_track"
    }
}