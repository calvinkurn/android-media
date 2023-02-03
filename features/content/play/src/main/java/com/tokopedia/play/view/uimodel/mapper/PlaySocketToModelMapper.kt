package com.tokopedia.play.view.uimodel.mapper

import com.google.gson.JsonObject
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.*
import com.tokopedia.play.data.multiplelikes.MultipleLikeConfig
import com.tokopedia.play.data.realtimenotif.RealTimeNotification
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.model.interactive.GetCurrentInteractiveResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.utils.date.DateUtil
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
@PlayScope
class PlaySocketToModelMapper @Inject constructor(
    private val productTagMapper: PlayProductTagUiMapper,
    private val merchantVoucherMapper: PlayMerchantVoucherUiMapper,
    private val chatMapper: PlayChatUiMapper,
    private val channelStatusMapper: PlayChannelStatusMapper,
    private val interactiveMapper: PlayInteractiveMapper,
    private val realTimeNotificationMapper: PlayRealTimeNotificationMapper,
    private val multipleLikesMapper: PlayMultipleLikesMapper,
    private val userWinnerStatusMapper: PlayUserWinnerStatusMapper,
) {

    fun mapTotalLike(input: TotalLike): Pair<Long, String> {
        return input.totalLike to input.totalLikeFormatted
    }

    fun mapTotalView(input: TotalView): String {
        return if (input.totalViewFormatted.isBlank()) "0" else input.totalViewFormatted
    }

    fun mapPinnedMessage(input: PinnedMessage): PinnedMessageUiModel {
        return PinnedMessageUiModel(
                id = input.pinnedMessageId,
                appLink = input.redirectUrl,
                title = input.title,
        )
    }

    fun mapQuickReplies(input: QuickReply): PlayQuickReplyInfoUiModel {
        return PlayQuickReplyInfoUiModel(input.data)
    }

    fun mapProductSection(input: ProductSection): TagItemUiModel {
        val controlTime = DateUtil.getCurrentDate()
        return TagItemUiModel(
            product = ProductUiModel(
                productSectionList = input.sectionList.map {
                    productTagMapper.mapSection(it, controlTime)
                },
                canShow = input.config.showProductTag,
            ),
            maxFeatured = input.config.peekProductCount,
            bottomSheetTitle = input.config.bottomSheetTitle,
            resultState = ResultState.Success,
            voucher = VoucherUiModel.Empty // set default value, because we're not updating voucher value
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun mapMerchantVoucher(input: MerchantVoucher, partnerName: String): VoucherUiModel {
        val vouchers = input.listOfVouchers.map(merchantVoucherMapper::mapMerchantVoucher)
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

    fun mapStatus(isBanned: Boolean): PlayStatusType {
        return channelStatusMapper.mapStatusBanned(isBanned)
    }

    fun mapInteractive(input: GiveawayResponse): GameUiModel.Giveaway {
        val waitingDurationInMillis = TimeUnit.SECONDS.toMillis(input.waitingDuration.toLong())
        return interactiveMapper.mapGiveaway(input, waitingDurationInMillis)
    }

    fun mapInteractive(input: GetCurrentInteractiveResponse.Data): GameUiModel {
        return interactiveMapper.mapInteractive(input)
    }

    fun mapRealTimeNotification(input: RealTimeNotification): RealTimeNotificationUiModel {
        return realTimeNotificationMapper.mapRealTimeNotification(input)
    }

    fun mapMultipleLikeConfig(
        configs: List<MultipleLikeConfig>
    ) : PlayLikeBubbleConfig {
        return multipleLikesMapper.mapMultipleLikeConfig(configs)
    }

    fun mapUserWinnerStatus(userWinnerStatus: UserWinnerStatus): PlayUserWinnerStatusUiModel {
        return userWinnerStatusMapper.mapUserWinnerStatus(userWinnerStatus)
    }

    fun mapQuizFromSocket(response: QuizResponse): GameUiModel {
        val waitingDurationInMillis = TimeUnit.SECONDS.toMillis(response.waitingDuration)
        return interactiveMapper.mapQuiz(response, waitingDurationInMillis)
    }

    /**
     * Send
     */
    fun mapSendChat(message: String, channelId: String): String {
        return parseSendMessage(message, channelId)
    }

    fun mapSendLike(channelId: String): String {
        val param = JsonObject()
        param.addProperty(PARAM_SEND_CHANNEL_ID, channelId.toLongOrZero())

        val bundle = JsonObject()
        bundle.addProperty(PARAM_SEND_TYPE, PARAM_SEND_TYPE_MULTIPLE_LIKE)
        bundle.add(PARAM_SEND_DATA, param)

        return bundle.toString()
    }

    private fun parseSendMessage(message: String, channelId: String): String {
        val param = JsonObject()
        param.addProperty(PARAM_SEND_CHANNEL_ID, channelId.toLongOrZero())
        param.addProperty(PARAM_SEND_MESSAGE, message)

        val bundle = JsonObject()
        bundle.addProperty(PARAM_SEND_TYPE, PARAM_SEND_TYPE_SEND)
        bundle.add(PARAM_SEND_DATA, param)

        return bundle.toString()
    }

    companion object {

        private const val PARAM_SEND_TYPE = "type"
        private const val PARAM_SEND_DATA = "data"
        private const val PARAM_SEND_TYPE_SEND = "SEND_MESG"
        private const val PARAM_SEND_TYPE_MULTIPLE_LIKE = "MULTIPLE_LIKE"
        private const val PARAM_SEND_CHANNEL_ID = "channel_id"
        private const val PARAM_SEND_MESSAGE = "message"
        private const val PARAM_SEND_WAREHOUSE_ID = "warehouse_id"
    }
}
