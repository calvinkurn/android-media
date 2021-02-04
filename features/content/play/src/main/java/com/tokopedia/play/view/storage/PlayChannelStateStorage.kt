package com.tokopedia.play.view.storage

import com.tokopedia.play.view.uimodel.recom.*

/**
 * Created by jegul on 19/01/21
 */
class PlayChannelStateStorage {

    private val playPageMap: MutableMap<String, PlayChannelData> = mutableMapOf()

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
        val totalViewInfo: PlayTotalViewUiModel,
        val shareInfo: PlayShareInfoUiModel,
        val cartInfo: PlayCartInfoUiModel,
        val pinnedInfo: PlayPinnedInfoUiModel,
        val quickReplyInfo: PlayQuickReplyInfoUiModel,
        val videoMetaInfo: PlayVideoMetaInfoUiModel,
        val statusInfo: PlayStatusInfoUiModel
)