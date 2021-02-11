package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.ChannelStatusResponse
import com.tokopedia.play.data.Product
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.play.data.Voucher
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowInfoUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.ui.PlayChatUiModel
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

    fun mapPartnerInfo(input: ShopInfo) = PlayPartnerFollowInfoUiModel(
            isFollowable = userSession.shopId != input.shopCore.shopId,
            isFollowed = input.favoriteData.alreadyFavorited == 1
    )
}