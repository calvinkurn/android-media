package com.tokopedia.play.broadcaster.model

import com.google.gson.Gson
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.CreateLiveStreamChannelResponse
import com.tokopedia.play.broadcaster.domain.model.GetLiveFollowersResponse
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.type.ProductStock
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import java.io.File
import java.util.*

/**
 * Created by jegul on 25/09/20
 */
class UiModelBuilder {

    private val gson = Gson()

    /**
     * Network Data
     */
    fun buildLiveStats(): GetLiveStatisticsResponse.ReportChannelSummary {
        return gson.fromJson(loadJsonToString("mock_live_stats.json"), GetLiveStatisticsResponse.ReportChannelSummary::class.java)
    }

    fun buildProductsInEtalase(): GetProductsByEtalaseResponse.GetProductListData {
        return gson.fromJson(loadJsonToString("mock_products_in_etalase.json"), GetProductsByEtalaseResponse::class.java).productList
    }

    fun buildCreateLiveStreamGetMedia(): CreateLiveStreamChannelResponse.GetMedia {
        return gson.fromJson(loadJsonToString("mock_create_live_stream.json"), CreateLiveStreamChannelResponse::class.java).media
    }

    fun buildGetLiveFollowers(): GetLiveFollowersResponse {
        return gson.fromJson(loadJsonToString("mock_get_live_followers.json"), GetLiveFollowersResponse::class.java)
    }

    /**
     * Pojo
     */
    fun buildProductData(
            id: String = "1",
            name: String = "Product 1",
            imageUrl: String = "https://www.tokopedia.com",
            originalImageUrl: String = "https://www.tokopedia.com",
            stock: ProductStock = StockAvailable(1)
    ) = ProductData(id, name, imageUrl, originalImageUrl, stock)

    private fun loadJsonToString(path: String): String {
        val file = File(this::class.java.classLoader!!.getResource(path).path)
        val stringBuilder = StringBuilder()
        file.useLines {
            it.forEach { text ->
                stringBuilder.append(text)
            }
        }
        return stringBuilder.toString()
    }

    fun buildPlayCoverUiModel(
            croppedCover: CoverSetupState = CoverSetupState.Blank,
            state: SetupDataState = SetupDataState.Draft
    ) = PlayCoverUiModel(
            croppedCover = croppedCover,
            state = state
    )

    fun buildConfigurationUiModel(
        streamAllowed: Boolean = true,
        channelId: String = "",
        channelType: ChannelType = ChannelType.Draft,
        remainingTime: Long = 0L,
        durationConfig: DurationConfigUiModel = buildDurationConfigUiModel(),
        productTagConfig: ProductTagConfigUiModel = buildProductTagConfigUiModel(),
        coverConfig: CoverConfigUiModel = buildCoverConfigUiModel(),
        countDown: Long = 0L,
        scheduleConfig: BroadcastScheduleConfigUiModel = buildBroadcastScheduleConfigUiModel(),
        tnc: List<TermsAndConditionUiModel> = emptyList(),
    ) = ConfigurationUiModel(
        streamAllowed = streamAllowed,
        channelId = channelId,
        channelType = channelType,
        remainingTime = remainingTime,
        durationConfig = durationConfig,
        productTagConfig = productTagConfig,
        coverConfig = coverConfig,
        countDown = countDown,
        scheduleConfig = scheduleConfig,
        tnc = tnc,
    )

    fun buildDurationConfigUiModel(
        duration: Long = 0L,
        pauseDuration: Long = 0L,
        maxDurationDesc: String = "",
        errorMessage: String = "",
    ) = DurationConfigUiModel(
        duration = duration,
        pauseDuration = pauseDuration,
        maxDurationDesc = maxDurationDesc,
        errorMessage = errorMessage,
    )

    fun buildProductTagConfigUiModel(
        maxProduct: Int = 0,
        minProduct: Int = 0,
        maxProductDesc: String = "",
        errorMessage: String = ""
    ) = ProductTagConfigUiModel(
        maxProduct = maxProduct,
        minProduct = minProduct,
        maxProductDesc = maxProductDesc,
        errorMessage = errorMessage,
    )

    fun buildCoverConfigUiModel(
        maxChars: Int = 0,
    ) = CoverConfigUiModel(
        maxChars = maxChars,
    )

    fun buildBroadcastScheduleConfigUiModel(
        minimum: Date = Date(),
        maximum: Date = Date(),
        default: Date = Date(),
    ) = BroadcastScheduleConfigUiModel(
        minimum = minimum,
        maximum = maximum,
        default = default,
    )

    fun buildTermsAndConditionUiModel(
        desc: String,
    ) = TermsAndConditionUiModel(
        desc = desc,
    )

    fun buildPinnedMessageUiModel(
        id: String = "",
        message: String = "",
        isActive: Boolean = true,
        editStatus: PinnedMessageEditStatus = PinnedMessageEditStatus.Nothing,
    ) = PinnedMessageUiModel(
        id = id,
        message = message,
        isActive = isActive,
        editStatus = editStatus,
    )
}