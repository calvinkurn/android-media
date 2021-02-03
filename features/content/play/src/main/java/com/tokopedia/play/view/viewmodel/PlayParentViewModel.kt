package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play_common.model.result.PageInfo
import com.tokopedia.play_common.model.result.PageResult
import com.tokopedia.play_common.model.result.PageResultState
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
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
        private val dispatchers: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
) : ViewModel() {

    class Factory @Inject constructor(
            private val playChannelStateStorage: PlayChannelStateStorage,
            private val getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase,
            private val playChannelMapper: PlayChannelDetailsWithRecomMapper,
            private val dispatchers: CoroutineDispatcherProvider,
            private val userSession: UserSessionInterface,
    ) {

        fun create(handle: SavedStateHandle): PlayParentViewModel {
            return PlayParentViewModel(
                    handle,
                    playChannelStateStorage,
                    getChannelDetailsWithRecomUseCase,
                    playChannelMapper,
                    dispatchers,
                    userSession
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

    private var mNextKey: GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey = GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey.ChannelId(
            channelId = handle[PLAY_KEY_CHANNEL_ID] ?: error("Channel ID must be provided"),
            sourceType = GetChannelDetailsWithRecomUseCase.SourceType.getBySource(
                    sourceType = handle[KEY_SOURCE_TYPE] ?: "",
                    sourceId = handle[KEY_SOURCE_ID]
            )
    )

    init {
        loadNextPage()
    }

    fun getLatestChannelStorageData(channelId: String): PlayChannelData = playChannelStateStorage.getData(channelId)

    fun setLatestChannelStorageData(
            channelId: String,
            data: PlayChannelData
    ) {
        playChannelStateStorage.setData(channelId, data)
    }

    fun loadNextPage() {
        getChannelDetailsWithRecom(mNextKey)
    }

    private fun getChannelDetailsWithRecom(nextKey: GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey) {
        _observableChannelIdsResult.value = PageResult.Loading(playChannelStateStorage.getChannelList())

        viewModelScope.launchCatchError(block = {
            withContext(dispatchers.io) {
                val response = getChannelDetailsWithRecomUseCase.apply {
                    params = GetChannelDetailsWithRecomUseCase.createParams(nextKey)
                }.executeOnBackground()

                mNextKey = GetChannelDetailsWithRecomUseCase.ChannelDetailNextKey.Cursor(response.channelDetails.meta.cursor)

                playChannelMapper.map(response).forEach {
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

    companion object {

        private const val KEY_SOURCE_TYPE = "source_type"
        private const val KEY_SOURCE_ID = "source_id"
    }
}