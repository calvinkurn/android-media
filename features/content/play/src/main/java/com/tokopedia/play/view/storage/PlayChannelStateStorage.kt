package com.tokopedia.play.view.storage

import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel

/**
 * Created by jegul on 19/01/21
 */
class PlayChannelStateStorage {

    private val playPageMap: MutableMap<String, PlayChannelData> = mutableMapOf()

    fun getData(channelId: String): PlayChannelData? = playPageMap[channelId]

    fun setData(channelId: String, channelData: PlayChannelData) {
        playPageMap[channelId] = channelData
    }

    fun getChannelList() = playPageMap.keys.toList()

    fun clearData() = playPageMap.clear()
}

data class PlayChannelData(
        val id: String,
        val channelDetail: PlayChannelDetailUiModel,
        val partnerInfo: PlayPartnerInfo,
        val likeInfo: PlayLikeInfoUiModel,
        val channelReportInfo: PlayChannelReportUiModel,
        val cartInfo: PlayCartInfoUiModel,
        val pinnedInfo: PlayPinnedInfoUiModel,
        val quickReplyInfo: PlayQuickReplyInfoUiModel,
        val videoMetaInfo: PlayVideoMetaInfoUiModel,
        val statusInfo: PlayStatusInfoUiModel,
        val leaderboardInfo: PlayLeaderboardInfoUiModel,
        val upcomingInfo: PlayUpcomingUiModel
)