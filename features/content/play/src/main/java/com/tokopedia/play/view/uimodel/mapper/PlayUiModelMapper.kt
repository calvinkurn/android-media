package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.play.data.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.domain.model.interactive.ChannelInteractive
import com.tokopedia.play_common.domain.model.interactive.GetInteractiveLeaderboardResponse
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
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
        private val interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper,
        private val playUserReportMapper: PlayUserReportReasoningMapper,
        private val cartMapper: PlayCartMapper,
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

    fun mapWaitingDuration(input: ChannelStatusResponse): Int {
        return channelStatusMapper.mapWaitingDurationResponse(input)
    }

    fun mapPartnerInfo(input: ShopInfo): Boolean {
        return input.favoriteData.alreadyFavorited == 1
    }

    fun mapInteractive(input: ChannelInteractive): PlayCurrentInteractiveModel {
        return channelInteractiveMapper.mapInteractive(input)
    }

    fun mapInteractiveLeaderboard(input: GetInteractiveLeaderboardResponse): PlayLeaderboardInfoUiModel {
        return interactiveLeaderboardMapper.mapLeaderboard(input) { false }
    }

    fun mapAddToCartFeedback(input: AddToCartDataModel): CartFeedbackResponseModel {
        return cartMapper.mapCartFeedbackResponse(input)
    }

    fun mapUserReport(input: List<UserReportOptions>): List<PlayUserReportReasoningUiModel> {
        return input.map(playUserReportMapper::mapUserReportReasoning)
    }

    fun mapUserReportSubmission(input: UserReportSubmissionResponse.Result): Boolean{
        return input.status == "success"
    }

    fun mapProfileHeader(input: ProfileHeader): String = input.profileInfo.encryptedUserId

    fun mapFollowingKol(input: List<FollowKOL.FollowedKOL>): Boolean = input.firstOrNull()?.status ?: false

    fun mapFollowKol(input: FollowInfo): Boolean = input.data.isSuccess
}