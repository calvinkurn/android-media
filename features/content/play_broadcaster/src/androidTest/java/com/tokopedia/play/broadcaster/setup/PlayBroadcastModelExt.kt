package com.tokopedia.play.broadcaster.setup

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.ui.model.*
import java.util.*

/**
 * Created by fachrizalmrsln on 28/09/22
 */
fun accountListResponse(
    shopEligible: Boolean = true,
    buyerHasUsername: Boolean = true,
    buyerHasAcceptTnc: Boolean = true
): List<ContentAccountUiModel> {
    return if (!shopEligible) {
        listOf(
            ContentAccountUiModel(
                id = "12345",
                type = ContentCommonUserType.TYPE_SHOP,
                name = "Shop",
                iconUrl = "icon.url.shop",
                badge = "icon.badge",
                hasUsername = false,
                enable = false,
            ),
            ContentAccountUiModel(
                id = "67890",
                type = ContentCommonUserType.TYPE_USER,
                name = "Buyer",
                iconUrl = "icon.url.buyer",
                badge = "icon.badge",
                hasUsername = buyerHasUsername,
                enable = buyerHasAcceptTnc,
            ),
        )
    } else {
        listOf(
            ContentAccountUiModel(
                id = "12345",
                type = ContentCommonUserType.TYPE_SHOP,
                name = "Shop",
                iconUrl = "icon.url.shop",
                badge = "icon.badge",
                hasUsername = true,
                enable = true,
            ),
            ContentAccountUiModel(
                id = "67890",
                type = ContentCommonUserType.TYPE_USER,
                name = "Buyer",
                iconUrl = "icon.url.buyer",
                badge = "icon.badge",
                hasUsername = buyerHasUsername,
                enable = buyerHasAcceptTnc,
            ),
        )
    }
}

val channelResponse = GetChannelResponse.Channel(
    basic = GetChannelResponse.ChannelBasic(
        coverUrl = "https://tokopedia.com",
        channelId = "12345",
    )
)

fun buildConfigurationUiModel(
    streamAllowed: Boolean = true,
    shortVideoAllowed: Boolean = false,
    channelId: String = "12345",
    channelStatus: ChannelStatus = ChannelStatus.Draft,
    durationConfig: DurationConfigUiModel = buildDurationConfigUiModel(),
    productTagConfig: ProductTagConfigUiModel = buildProductTagConfigUiModel(),
    coverConfig: CoverConfigUiModel = buildCoverConfigUiModel(),
    countDown: Long = 0L,
    scheduleConfig: BroadcastScheduleConfigUiModel = buildBroadcastScheduleConfigUiModel(),
    tnc: List<TermsAndConditionUiModel> = emptyList(),
) = ConfigurationUiModel(
    streamAllowed = streamAllowed,
    shortVideoAllowed = shortVideoAllowed,
    channelId = channelId,
    channelStatus = channelStatus,
    durationConfig = durationConfig,
    productTagConfig = productTagConfig,
    coverConfig = coverConfig,
    countDown = countDown,
    scheduleConfig = scheduleConfig,
    tnc = tnc,
)

private fun buildDurationConfigUiModel(
    remainingDuration: Long = 0L,
    pauseDuration: Long = 0L,
    maxDuration: Long = 0L,
    maxDurationDesc: String = "",
) = DurationConfigUiModel(
    remainingDuration = remainingDuration,
    pauseDuration = pauseDuration,
    maxDuration = maxDuration,
    maxDurationDesc = maxDurationDesc,
)

private fun buildProductTagConfigUiModel(
    maxProduct: Int = 0,
    minProduct: Int = 0,
    maxProductDesc: String = "",
    errorMessage: String = "",
) = ProductTagConfigUiModel(
    maxProduct = maxProduct,
    minProduct = minProduct,
    maxProductDesc = maxProductDesc,
    errorMessage = errorMessage,
)

private fun buildCoverConfigUiModel(
    maxChars: Int = 0,
) = CoverConfigUiModel(
    maxChars = maxChars,
)

private fun buildBroadcastScheduleConfigUiModel(
    minimum: Date = Date(),
    maximum: Date = Date(),
    default: Date = Date(),
) = BroadcastScheduleConfigUiModel(
    minimum = minimum,
    maximum = maximum,
    default = default,
)
