package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.ChannelStatusResponse
import com.tokopedia.play.data.Product
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.play.data.Voucher
import com.tokopedia.play.data.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play.data.interactive.ChannelInteractive
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowInfo
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.domain.model.interactive.GetInteractiveLeaderboardResponse
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
class PlayUiModelMapper @Inject constructor(
        private val userSession: UserSessionInterface,
        private val productTagMapper: PlayProductTagUiMapper,
        private val merchantVoucherMapper: PlayMerchantVoucherUiMapper,
        private val chatMapper: PlayChatUiMapper,
        private val channelStatusMapper: PlayChannelStatusMapper,
        private val channelInteractiveMapper: PlayChannelInteractiveMapper,
        private val interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper,
) {

    fun mapProductTags(input: List<Product>): List<PlayProductUiModel> {
        return input.map(productTagMapper::mapProductTag)
    }

    fun mapMerchantVouchers(input: List<Voucher>): List<MerchantVoucherUiModel> {
        return input.map(merchantVoucherMapper::mapMerchantVoucher)
    }

    fun mapChat(input: PlayChat): PlayChatUiModel {
        return chatMapper.mapChat(input)
    }

    fun mapStatus(input: ChannelStatusResponse): PlayStatusType {
        return channelStatusMapper.mapStatusFromResponse(input)
    }

    fun mapPartnerInfo(input: ShopInfo) = PlayPartnerFollowInfo(
            isOwnShop = userSession.shopId == input.shopCore.shopId,
            isFollowing = input.favoriteData.alreadyFavorited == 1
    )

    fun mapInteractive(input: ChannelInteractive): PlayCurrentInteractiveModel {
        return channelInteractiveMapper.mapInteractive(input)
    }

    fun mapInteractiveLeaderboard(input: GetInteractiveLeaderboardResponse): PlayLeaderboardInfoUiModel {
        return interactiveLeaderboardMapper.mapLeaderboard(input)
    }
}