package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.uimodel.PlayChatHistoryUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.domain.model.interactive.GetCurrentInteractiveResponse
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.usecase.interactive.GetLeaderboardSlotResponse
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.utils.date.DateUtil
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
class PlayUiModelMapper @Inject constructor(
    private val productTagMapper: PlayProductTagUiMapper,
    private val merchantVoucherMapper: PlayMerchantVoucherUiMapper,
    private val chatMapper: PlayChatUiMapper,
    private val channelStatusMapper: PlayChannelStatusMapper,
    private val channelInteractiveMapper: PlayChannelInteractiveMapper,
    private val interactiveMapper: PlayInteractiveMapper,
    private val interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper,
    private val playUserReportMapper: PlayUserReportReasoningMapper,
    private val cartMapper: PlayCartMapper,
) {

    fun mapProductSection(input: List<Section>): List<ProductSectionUiModel.Section> {
        val controlTime = DateUtil.getCurrentDate()
        return input.map {
            productTagMapper.mapSection(it, controlTime)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun mapMerchantVouchers(input: List<Voucher>, partnerName: String): VoucherUiModel {
        val vouchers = input.map(merchantVoucherMapper::mapMerchantVoucher)
        val eligibleForShown = vouchers.find { !it.isPrivate }
        val newVoucher = buildList {
            if(eligibleForShown != null) add(PlayVoucherUiModel.InfoHeader(partnerName))
            addAll(vouchers)
        }
        return VoucherUiModel(newVoucher)
    }

    fun mapChat(input: PlayChat): PlayChatUiModel {
        return chatMapper.mapChat(input)
    }

    fun mapHistoryChat(response: PlayChatHistoryResponse): PlayChatHistoryUiModel {
        return PlayChatHistoryUiModel(
            chatList = response.wrapper.data.map {
                mapChat(it)
            },
            nextCursor = response.wrapper.pagination.nextCursor
        )
    }

    fun mapStatus(input: ChannelStatusResponse): PlayStatusType {
        return channelStatusMapper.mapStatusFromResponse(input)
    }

    fun mapWaitingDuration(input: ChannelStatusResponse): Int {
        return channelStatusMapper.mapWaitingDurationResponse(input)
    }

    fun mapPartnerInfo(input: ShopInfo): Boolean {
        return input.favoriteData.alreadyFavorited == 1
    }

    fun mapInteractive(input: GiveawayResponse): PlayCurrentInteractiveModel {
        return channelInteractiveMapper.mapInteractive(input)
    }

    fun mapInteractive(input: GetCurrentInteractiveResponse.Data): GameUiModel {
        return interactiveMapper.mapInteractive(input)
    }

    fun mapInteractiveLeaderboard(input: GetLeaderboardSlotResponse): List<LeaderboardGameUiModel> {
        return interactiveLeaderboardMapper.mapNewPlayLeaderboard(input) { false }
    }

    fun mapAddToCartFeedback(input: AddToCartDataModel): CartFeedbackResponseModel {
        return cartMapper.mapCartFeedbackResponse(input)
    }

    fun mapUserReport(input: List<UserReportOptions>): List<PlayUserReportReasoningUiModel> {
        return input.map(playUserReportMapper::mapUserReportReasoning)
    }

    fun mapUserReportSubmission(input: UserReportSubmissionResponse.Result): Boolean {
        return input.status == "success"
    }

    fun mapFollowingKol(input: List<FollowKOL.FollowedKOL>): Pair<Boolean, String> = Pair(input.firstOrNull()?.status ?: false, input.firstOrNull()?.encryptedUserId ?: "")

    fun mapUnfollowKol(input: KOLUnFollowStatus): Boolean {
        return input.unFollowedKOLInfo.data.isSuccess == 1
    }

    fun mapVariantChildToProduct(
        child: VariantChild,
        prevDetail: PlayProductUiModel.Product,
    ): PlayProductUiModel.Product {
        val stock = child.stock

        return PlayProductUiModel.Product(
            id = child.productId,
            shopId = prevDetail.shopId,
            imageUrl = child.picture?.original.orEmpty(),
            title = child.name,
            stock = if (stock == null) OutOfStock
            else StockAvailable(stock.stock ?: 0),
            isVariantAvailable = true,
            price = if (child.campaign?.discountedPercentage != 0f) {
                DiscountedPrice(
                    originalPrice = child.campaign?.originalPriceFmt.toEmptyStringIfNull(),
                    discountedPriceNumber = child.campaign?.discountedPrice ?: 0.0,
                    discountPercent = child.campaign?.discountedPercentage?.toLong() ?: 0,
                    discountedPrice = child.campaign?.discountedPriceFmt.toEmptyStringIfNull()
                )
            } else {
                OriginalPrice(child.priceFmt.toEmptyStringIfNull(), child.price)
            },
            minQty = prevDetail.minQty.orZero(),
            isFreeShipping = prevDetail.isFreeShipping,
            applink = prevDetail.applink,
            isTokoNow = prevDetail.isTokoNow,
            isPinned = prevDetail.isPinned,
            isRilisanSpesial = prevDetail.isRilisanSpesial,
            buttons = prevDetail.buttons,
        )
    }
}
