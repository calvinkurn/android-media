package com.tokopedia.play.broadcaster.model

import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.CreateLiveStreamChannelResponse
import com.tokopedia.play.broadcaster.domain.model.GetLiveFollowersResponse
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.type.PriceUnknown
import com.tokopedia.play.broadcaster.type.ProductPrice
import com.tokopedia.play.broadcaster.type.ProductStock
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.CoverConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.DurationConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductTagConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetPage
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetType
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.BroadcasterCheckAffiliateResponseUiModel
import com.tokopedia.play.broadcaster.ui.model.shortsaffiliate.OnboardAffiliateUiModel
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagItem
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import io.mockk.mockk
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
            stock: ProductStock = StockAvailable(1),
            price: ProductPrice = PriceUnknown,
    ) = ProductData(id, name, imageUrl, originalImageUrl, stock, price)

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

    fun buildConfigurationUiModel(
        streamAllowed: Boolean = true,
        shortVideoAllowed: Boolean = false,
        hasContent: Boolean = false,
        channelId: String = "",
        channelStatus: ChannelStatus = ChannelStatus.Draft,
        durationConfig: DurationConfigUiModel = buildDurationConfigUiModel(),
        productTagConfig: ProductTagConfigUiModel = buildProductTagConfigUiModel(),
        coverConfig: CoverConfigUiModel = buildCoverConfigUiModel(),
        countDown: Long = 0L,
        scheduleConfig: BroadcastScheduleConfigUiModel = buildBroadcastScheduleConfigUiModel(),
        tnc: List<TermsAndConditionUiModel> = emptyList(),
        beautificationConfig: BeautificationConfigUiModel = BeautificationConfigUiModel.Empty,
        showSaveButton: Boolean = false,
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
        beautificationConfig = beautificationConfig,
        showSaveButton = showSaveButton,
    )

    fun buildDurationConfigUiModel(
        remainingDuration: Long = 0L,
        pauseDuration: Long = 0L,
        maxDuration: Long = 0L,
        maxDurationDesc: String = ""
    ) = DurationConfigUiModel(
        remainingDuration = remainingDuration,
        pauseDuration = pauseDuration,
        maxDuration = maxDuration,
        maxDurationDesc = maxDurationDesc,
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

    fun buildException(message: String = "Network Error") = Exception(message)

    fun buildQuizModel(
        id: String = "",
        title: String = "",
        waitingDuration: Long = 0,
        duration: Int = 0,
        choices: List<QuizChoicesUiModel> = emptyList(),
    ): GameUiModel.Quiz {
        return GameUiModel.Quiz(
            id = id,
            title = title,
            waitingDuration = waitingDuration,
            status = GameUiModel.Quiz.Status.Ongoing(Calendar.getInstance().apply {
                add(Calendar.SECOND, duration)
            }),
            listOfChoices = choices,
        )
    }

    fun buildAccountListModel(
        idShop: String = "1234",
        idBuyer: String = "5678",
        tncShop: Boolean = true,
        usernameShop: Boolean = true,
        tncBuyer: Boolean = true,
        usernameBuyer: Boolean = true,
        onlyShop: Boolean = false,
        onlyBuyer: Boolean = false
    ): List<ContentAccountUiModel> {
        return when {
            onlyShop -> listOf(
                ContentAccountUiModel(
                    id = idShop,
                    type = TYPE_SHOP,
                    name = "Shop",
                    iconUrl = "icon.url.shop",
                    badge = "icon.badge",
                    hasUsername = usernameShop,
                    hasAcceptTnc = tncShop,
                    enable = tncShop
                )
            )
            onlyBuyer -> listOf(
                ContentAccountUiModel(
                    id = idBuyer,
                    type = TYPE_USER,
                    name = "Buyer",
                    iconUrl = "icon.url.buyer",
                    badge = "icon.badge",
                    hasUsername = usernameBuyer,
                    hasAcceptTnc = tncBuyer,
                    enable = tncBuyer
                )
            )
            else -> listOf(
                ContentAccountUiModel(
                    id = idShop,
                    type = TYPE_SHOP,
                    name = "Shop",
                    iconUrl = "icon.url.shop",
                    badge = "icon.badge",
                    hasUsername = usernameShop,
                    hasAcceptTnc = tncShop,
                    enable = tncShop
                ),
                ContentAccountUiModel(
                    id = idBuyer,
                    type = TYPE_USER,
                    name = "Buyer",
                    iconUrl = "icon.url.buyer",
                    badge = "icon.badge",
                    hasUsername = usernameBuyer,
                    hasAcceptTnc = tncBuyer,
                    enable = tncBuyer
                ),
            )
        }
    }

    fun buildCoverSetupStateUploaded(
        localImage: Uri? = mockk(relaxed = true),
        coverImage: Uri = mockk(relaxed = true),
        coverSource: CoverSource = mockk(relaxed = true),
    ) = CoverSetupState.Cropped.Uploaded(
        localImage = localImage,
        coverImage = coverImage,
        coverSource = coverSource,
    )

    fun buildTickerBottomSheetResponse(
        page: TickerBottomSheetPage = TickerBottomSheetPage.UNKNOWN,
        type: TickerBottomSheetType = TickerBottomSheetType.UNKNOWN,
    ): TickerBottomSheetUiModel {
        return TickerBottomSheetUiModel(
            mainText = listOf(
                TickerBottomSheetUiModel.MainText(
                    action = listOf(
                        TickerBottomSheetUiModel.Action(
                            item = "key item 1",
                            text = "text link",
                            link = "tokopedia.com",
                        )
                    ),
                    title = "Test Title",
                    description = "Test Description",
                )
            ),
            page = page,
            type = type,
            imageURL = "tokopedia.com",
            bottomText = TickerBottomSheetUiModel.BottomText(
                action = listOf(
                    TickerBottomSheetUiModel.Action(
                        item = "key item 1",
                        text = "text link",
                        link = "tokopedia.com",
                    )
                ),
                description = "Test Description"
            )
        )
    }

    fun buildTags(
        size: Int = 5,
        minTags: Int = 1,
        maxTags: Int = 2,
    ): PlayTagUiModel {
        return PlayTagUiModel(
            tags = mutableSetOf<PlayTagItem>().apply {
                for(i in 0 until size) {
                    add(
                        PlayTagItem(
                            tag = "Tag $i",
                            isChosen = false,
                            isActive = true,
                        )
                    )
                }
            },
            minTags = minTags,
            maxTags = maxTags,
        )
    }

    fun buildTncList(
        size: Int = 3,
    ) = List(size) {
        TermsAndConditionUiModel(desc = "Desc $it")
    }

    fun buildShortsConfig(
        shortsId: String = "123",
        shortsAllowed: Boolean = true,
        hasContent: Boolean = true,
        isBanned: Boolean = false,
        tncList: List<TermsAndConditionUiModel> = buildTncList(),
        maxTitleCharacter: Int = 24,
        maxTaggedProduct: Int = 30,
        shortsVideoSourceId: String = "asdf",
    ) = PlayShortsConfigUiModel(
        shortsId = shortsId,
        shortsAllowed = shortsAllowed,
        isBanned = isBanned,
        tncList = tncList,
        maxTitleCharacter = maxTitleCharacter,
        maxTaggedProduct = maxTaggedProduct,
        shortsVideoSourceId = shortsVideoSourceId,
        hasContent = hasContent,
    )

    fun buildSubmitOnboardAffiliate(
        errorMessage: String = ""
    ) = OnboardAffiliateUiModel(errorMessage)

    fun buildBroadcasterCheckAffiliate(
        affiliateName: String = "jonathan",
        isAffiliate: Boolean = true,
    ) = BroadcasterCheckAffiliateResponseUiModel(
        affiliateName = affiliateName,
        isAffiliate = isAffiliate,
    )
}
