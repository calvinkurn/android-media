package com.tokopedia.play.view.storage

import com.tokopedia.play.view.uimodel.recom.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by jegul on 19/01/21
 */
class PlayChannelStateStorage {

    private val playPageMap: MutableMap<String, PlayChannelData> = mutableMapOf()

    init {
//        playPageMap.putAll(
//                listOf("12665", "12668", "12669", "12670", "12672")
//                        .mapIndexed { index, cid ->
//                            cid to PlayChannelData.Placeholder(
//                                    if (index % 2 == 0) PinnedMessageUiModel(null, "alola", "alolan pokemon") else null,
//                                    if (index % 3 == 0) PinnedProductUiModel("alola", "Beli", false) else null
//                            )
//                        }
//        )
    }

    fun getData(channelId: String): PlayChannelData = playPageMap[channelId] ?: error("Channel not found")

    fun setData(channelId: String, channelData: PlayChannelData) {
        playPageMap[channelId] = channelData
    }

    fun getChannelList() = playPageMap.keys.toList()

}

data class PlayChannelData(
        val id: String,
        val partnerInfo: PlayPartnerInfoUiModel,
        val likeInfo: PlayLikeInfoUiModel,
        val shareInfo: PlayShareInfoUiModel,
        val cartInfo: PlayCartInfoUiModel,
        val pinnedInfo: PlayPinnedInfoUiModel,
        val quickReplyInfo: PlayQuickReplyInfoUiModel,
        val videoMetaInfo: PlayVideoMetaInfoUiModel,
)

//sealed class PlayChannelData {
//
//    object Empty : PlayChannelData()
//
//    data class Placeholder(
//            val id: String,
//            val partnerInfo: PlayPartnerInfoUiModel,
//            val likeParamInfo: PlayLikeParamInfoUiModel,
//            val shareInfo: PlayShareInfoUiModel,
//            val cartInfo: PlayCartInfoUiModel,
//            val pinnedInfo: PlayPinnedInfoUiModel,
//            val quickReplyInfo: PlayQuickReplyInfoUiModel,
////            val videoMetaInfo: PlayVideoMetaInfoUiModel,
//            val miscConfigInfo: PlayMiscConfigUiModel,
//    ) : PlayChannelData()
//
//    data class Complete(
//            val id: String,
//            val partnerInfo: PlayPartnerInfoUiModel,
//            val likeInfo: PlayLikeInfoUiModel,
//            val shareInfo: PlayShareInfoUiModel,
//            val cartInfo: PlayCartInfoUiModel,
//            val pinnedInfo: PlayPinnedInfoUiModel,
//            val quickReplyInfo: PlayQuickReplyInfoUiModel,
//
////            val pinnedMessage: PinnedMessageUiModel?,
////            val pinnedProduct: PinnedProductUiModel?,
//    ) : PlayChannelData()
//}