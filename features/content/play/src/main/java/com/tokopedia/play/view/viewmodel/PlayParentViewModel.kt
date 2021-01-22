package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.mapper.PlayChannelResponseMapper
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 19/01/21
 */
class PlayParentViewModel constructor(
        private val handle: SavedStateHandle,
        private val playChannelStateStorage: PlayChannelStateStorage,
        private val getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase,
        private val playChannelMapper: PlayChannelResponseMapper,
        private val dispatchers: CoroutineDispatcherProvider,
) : ViewModel() {

    class Factory @Inject constructor(
            private val playChannelStateStorage: PlayChannelStateStorage,
            private val getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase,
            private val playChannelMapper: PlayChannelResponseMapper,
            private val dispatchers: CoroutineDispatcherProvider,
    ) {

        fun create(handle: SavedStateHandle): PlayParentViewModel {
            return PlayParentViewModel(
                    handle,
                    playChannelStateStorage,
                    getChannelDetailsWithRecomUseCase,
                    playChannelMapper,
                    dispatchers
            )
        }
    }

    /**
     * LiveData
     */
    val observableChannelIdList: LiveData<List<String>>
        get() = _observableChannelIdList
    private val _observableChannelIdList = MutableLiveData<List<String>>()

    private var mNextKey: ChannelDetailNextKey = ChannelDetailNextKey.ChannelId(handle[PLAY_KEY_CHANNEL_ID] ?: error("Channel ID must be provided"))

    init {
        getChannelDetailsWithRecom(mNextKey)
    }

    fun getLatestChannelStorageData(channelId: String): PlayChannelData = playChannelStateStorage.getData(channelId)

    fun setLatestChannelStorageData(
            channelId: String,
            data: PlayChannelData
    ) {
        playChannelStateStorage.setData(channelId, data)
    }

    private fun getChannelDetailsWithRecom(nextKey: ChannelDetailNextKey) {
        viewModelScope.launch {
            withContext(dispatchers.io) {
                val response = getChannelDetailsWithRecomUseCase.apply {
                    params = when (nextKey) {
                        is ChannelDetailNextKey.ChannelId -> GetChannelDetailsWithRecomUseCase.createParamsWithChannelId(nextKey.channelId)
                        is ChannelDetailNextKey.Cursor -> GetChannelDetailsWithRecomUseCase.createParamsWithChannelId(nextKey.cursor)
                    }
                }.executeOnBackground()

                mNextKey = ChannelDetailNextKey.Cursor(response.channelDetails.meta.cursor)

                playChannelMapper.map(response).forEach {
                    playChannelStateStorage.setData(it.id, it)
                }
            }

            _observableChannelIdList.value = playChannelStateStorage.getChannelList()
        }

    }

    sealed class ChannelDetailNextKey {

        data class ChannelId(val channelId: String) : ChannelDetailNextKey()
        data class Cursor(val cursor: String) : ChannelDetailNextKey()
    }
}