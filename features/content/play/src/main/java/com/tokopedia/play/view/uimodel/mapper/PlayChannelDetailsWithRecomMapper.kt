package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.PartnerFolowStatus
import com.tokopedia.play.view.uimodel.recom.*

/**
 * Created by jegul on 21/01/21
 */
typealias PlayChannelResponseMapper = PlayViewerMapper<ChannelDetailsWithRecomResponse, List<PlayChannelData.Placeholder>>

class PlayChannelDetailsWithRecomMapper : PlayChannelResponseMapper {

    override fun map(input: ChannelDetailsWithRecomResponse): List<PlayChannelData.Placeholder> {
        return input.channelDetails.dataList.map {
            PlayChannelData.Placeholder(
                    id = it.id,
                    partnerInfo = mapPartnerInfo(it.partner),
                    likeInfo = mapLikeInfo(it.config.feedLikeParam),
                    shareInfo = mapShareInfo(it.share, it.config.active, it.config.freezed),
                    cartInfo = mapCartInfo(it.config),
                    miscConfigInfo = mapMiscConfigInfo(it.config),
            )
        }
    }

    private fun mapPartnerInfo(partnerResponse: ChannelDetailsWithRecomResponse.Partner) = PlayPartnerInfoUiModel(
            id = partnerResponse.id.toLongOrZero(),
            name = partnerResponse.name,
            type = PartnerType.getTypeByValue(partnerResponse.type),
            followStatus = PartnerFolowStatus.Unknown,
            isFollowable = false
    )

    private fun mapLikeInfo(feedLikeParamResponse: ChannelDetailsWithRecomResponse.FeedLikeParam) = PlayLikeInfoUiModel(
            contentId = feedLikeParamResponse.contentId,
            contentType = feedLikeParamResponse.contentType,
            likeType = feedLikeParamResponse.likeType
    )

    private fun mapShareInfo(shareResponse: ChannelDetailsWithRecomResponse.Share, isActive: Boolean, isFreezed: Boolean): PlayShareInfoUiModel {
        val fullShareContent = try {
            shareResponse.text.replace("${'$'}{url}", shareResponse.redirectUrl)
        } catch (e: Throwable) {
            "${shareResponse.text}/n${shareResponse.redirectUrl}"
        }

        return PlayShareInfoUiModel(
                content = fullShareContent,
                shouldShow = shareResponse.isShowButton
                        && shareResponse.redirectUrl.isNotBlank()
                        && isActive
                        && !isFreezed
        )
    }

    private fun mapCartInfo(configResponse: ChannelDetailsWithRecomResponse.Config) = PlayCartInfoUiModel(
            shouldShow = configResponse.showCart,
            count = 0
    )

    private fun mapMiscConfigInfo(configResponse: ChannelDetailsWithRecomResponse.Config) = PlayMiscConfigUiModel(
            shouldShowCart = configResponse.showCart
    )
}