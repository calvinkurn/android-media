package com.tokopedia.play.broadcaster.setup

import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.CoverConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.DurationConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductTagConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.PresetFilterUiModel
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUiModel
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
                hasAcceptTnc = false,
                enable = false,
            ),
            ContentAccountUiModel(
                id = "67890",
                type = ContentCommonUserType.TYPE_USER,
                name = "Buyer",
                iconUrl = "icon.url.buyer",
                badge = "icon.badge",
                hasUsername = buyerHasUsername,
                hasAcceptTnc = buyerHasAcceptTnc,
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
                hasAcceptTnc = true,
                enable = true,
            ),
            ContentAccountUiModel(
                id = "67890",
                type = ContentCommonUserType.TYPE_USER,
                name = "Buyer",
                iconUrl = "icon.url.buyer",
                badge = "icon.badge",
                hasUsername = buyerHasUsername,
                hasAcceptTnc=  buyerHasAcceptTnc,
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

val channelWithTitleResponse = GetChannelResponse.Channel(
    basic = GetChannelResponse.ChannelBasic(
        title = "Title",
        coverUrl = "https://tokopedia.com",
        channelId = "12345",
    )
)

val channelPausedResponse = GetChannelResponse.Channel(
    basic = GetChannelResponse.ChannelBasic(
        title = "Title",
        coverUrl = "https://tokopedia.com",
        channelId = "12345",
        status = GetChannelResponse.ChannelBasicStatus(
            id = "3",
            text = "pause",
        )
    )
)

fun buildConfigurationUiModel(
    streamAllowed: Boolean = true,
    shortVideoAllowed: Boolean = false,
    showSaveButton: Boolean = false,
    hasContent: Boolean = false,
    channelId: String = "12345",
    channelStatus: ChannelStatus = ChannelStatus.Draft,
    durationConfig: DurationConfigUiModel = buildDurationConfigUiModel(),
    productTagConfig: ProductTagConfigUiModel = buildProductTagConfigUiModel(),
    coverConfig: CoverConfigUiModel = buildCoverConfigUiModel(),
    countDown: Long = 0L,
    scheduleConfig: BroadcastScheduleConfigUiModel = buildBroadcastScheduleConfigUiModel(),
    tnc: List<TermsAndConditionUiModel> = emptyList(),
    beautificationConfig: BeautificationConfigUiModel = buildBeautificationConfig(),
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
    hasContent = hasContent,
    showSaveButton = showSaveButton,
    beautificationConfig = beautificationConfig,
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

fun buildBroadcastingConfigUiModel(): BroadcastingConfigUiModel {
    return BroadcastingConfigUiModel(
        audioRate = "123",
        bitrateMode = "123",
        fps = "123",
        maxRetry = 1,
        reconnectDelay = 1,
        videoBitrate = "123",
        videoWidth = "123",
        videoHeight = "123",
    )
}

fun buildBeautificationConfig(
    presetsSize: Int = 5,
    assetStatus: BeautificationAssetStatus = BeautificationAssetStatus.Available,
): BeautificationConfigUiModel {
    return BeautificationConfigUiModel(
        licenseLink = "licenseLink",
        modelLink = "modelLink",
        customFaceAssetLink = "customFaceAssetLink",
        faceFilters = List(4) {
            FaceFilterUiModel(
                id = when (it) {
                    0 -> "none"
                    1 -> "sharpen"
                    2 -> "buffing"
                    3 -> "toning"
                    else -> ""
                },
                name = "Face Filter $it",
                active = false,
                minValue = 0.0,
                maxValue = 1.0,
                defaultValue = 0.1 * it,
                value = 0.1 * it,
                isSelected = false,
            )
        },
        presets = List(presetsSize) {
            PresetFilterUiModel(
                id = if (it == 0) "none" else it.toString(),
                name = "Preset $it",
                active = false,
                minValue = 0.0,
                maxValue = 1.0,
                defaultValue = 0.1 * it,
                value = 0.1 * it,
                iconUrl = "iconUrl $it",
                assetLink = "assetLink $it",
                assetStatus = if (it == 0) BeautificationAssetStatus.Available else assetStatus,
                isSelected = false,
            )
        }
    )
}
