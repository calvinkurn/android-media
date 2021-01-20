package com.tokopedia.play.view.viewmodel

import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import javax.inject.Inject

/**
 * Created by jegul on 19/01/21
 */
class PlayParentViewModel @Inject constructor(
        private val playChannelStateStorage: PlayChannelStateStorage,
        dispatchers: CoroutineDispatcherProvider
) : PlayBaseViewModel(dispatchers.main) {

    fun getLatestChannelStorageData(channelId: String): PlayChannelData = playChannelStateStorage.getData(channelId)

    fun setLatestChannelStorageData(
            channelId: String,
            data: PlayChannelData.Complete
    ) {
        playChannelStateStorage.setData(channelId, data)
    }
}