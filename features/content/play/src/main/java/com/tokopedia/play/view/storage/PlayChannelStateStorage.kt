package com.tokopedia.play.view.storage

import com.tokopedia.play.view.uimodel.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.PinnedProductUiModel
import com.tokopedia.play.view.uimodel.PinnedRemoveUiModel
import com.tokopedia.play.view.uimodel.PinnedUiModel

/**
 * Created by jegul on 19/01/21
 */
class PlayChannelStateStorage {

    private val playPageMap: MutableMap<String, PlayChannelStorageData> = mutableMapOf()

    init {
        playPageMap.putAll(
                listOf("12665", "12668", "12669", "12670", "12672")
                        .mapIndexed { index, cid ->
                            cid to PlayChannelStorageData.Placeholder(
                                    cid,
                                    if (index % 2 == 0) PinnedMessageUiModel(null, "alola", "alolan pokemon") else null,
                                    if (index % 3 == 0) PinnedProductUiModel("alola", "Beli", false) else null
                            )
                        }
        )
    }

    fun getData(channelId: String): PlayChannelStorageData = playPageMap[channelId] ?: PlayChannelStorageData.Empty(channelId)

}

sealed class PlayChannelStorageData {

    abstract val channelId: String

    data class Empty(override val channelId: String) : PlayChannelStorageData()

    data class Placeholder(
            override val channelId: String,
            val pinnedMessage: PinnedMessageUiModel?,
            val pinnedProduct: PinnedProductUiModel?,
    ) : PlayChannelStorageData()

    data class Complete(
            override val channelId: String,
            val pinnedMessage: PinnedMessageUiModel?,
            val pinnedProduct: PinnedProductUiModel?,
    ) : PlayChannelStorageData()
}