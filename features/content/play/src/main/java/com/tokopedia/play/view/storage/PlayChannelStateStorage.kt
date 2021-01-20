package com.tokopedia.play.view.storage

import com.tokopedia.play.view.uimodel.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.PinnedProductUiModel
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by jegul on 19/01/21
 */
class PlayChannelStateStorage {

    private val playPageMap: ConcurrentHashMap<String, PlayChannelData> = ConcurrentHashMap()

    init {
        playPageMap.putAll(
                listOf("12665", "12668", "12669", "12670", "12672")
                        .mapIndexed { index, cid ->
                            cid to PlayChannelData.Placeholder(
                                    if (index % 2 == 0) PinnedMessageUiModel(null, "alola", "alolan pokemon") else null,
                                    if (index % 3 == 0) PinnedProductUiModel("alola", "Beli", false) else null
                            )
                        }
        )
    }

    fun getData(channelId: String): PlayChannelData = playPageMap[channelId] ?: PlayChannelData.Empty

    fun setData(channelId: String, channelData: PlayChannelData) {
        playPageMap[channelId] = channelData
    }

}

sealed class PlayChannelData {

    object Empty : PlayChannelData()

    data class Placeholder(
            val pinnedMessage: PinnedMessageUiModel?,
            val pinnedProduct: PinnedProductUiModel?,
    ) : PlayChannelData()

    data class Complete(
            val pinnedMessage: PinnedMessageUiModel?,
            val pinnedProduct: PinnedProductUiModel?,
    ) : PlayChannelData()
}