package com.tokopedia.feedplus.browse.data

import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play.widget.ui.model.PlayGridType
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseRepositoryImpl @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val playWidgetSlotUseCase: GetPlayWidgetSlotUseCase,
    private val mapper: FeedBrowseMapper
) : FeedBrowseRepository {

    override suspend fun getTitle(): String {
        feedXHeaderUseCase.setRequestParams(
            FeedXHeaderUseCase.createParam(
                listOf(
                    FeedXHeaderRequestFields.BROWSE.value
                )
            )
        )
        val headerData = feedXHeaderUseCase.executeOnBackground()
        return headerData.feedXHeaderData.data.browse.title
    }

    // todo: with real data
    override suspend fun getSlots(): List<FeedBrowseUiModel> {
        delay(500)
        return listOf(
            FeedBrowseUiModel.Placeholder(""),
            FeedBrowseUiModel.Placeholder(""),
            FeedBrowseUiModel.Placeholder(""),
            FeedBrowseUiModel.Placeholder("")
        )
    }

    // todo: with real data
    override suspend fun getCards(type: String): FeedBrowseUiModel.Cards {
        delay(500)
        return FeedBrowseUiModel.Cards(
            title = "Sesuai sama kesukaanmu",
            chips = listOf(
                FeedBrowseChipUiModel("", "Rekomendasi KOL"),
                FeedBrowseChipUiModel("", "Kuliner Seru"),
                FeedBrowseChipUiModel("", "Inspirasi Fashion"),
                FeedBrowseChipUiModel("", "Beauty"),
                FeedBrowseChipUiModel("", "Home & Living")
            ),
            model = PlayWidgetUiModel(
                title = "",
                actionTitle = "",
                actionAppLink = "",
                isActionVisible = false,
                config = PlayWidgetConfigUiModel.Empty,
                background = PlayWidgetBackgroundUiModel.Empty,
                items = listOf(
                    dummyChannel(
                        channelType = PlayWidgetChannelType.Vod,
                        totalView = "13rb",
                        coverUrl = "https://images.tokopedia.net/img/jJtrdn/2023/6/21/0ed85c66-9b2f-4952-ad46-62a89e51c4d1.jpg?b=U36aVKEMDiE3%7DpI%3B-pNH9Gay-pxZPC%24%259F%24%25"
                    ),
                    dummyChannel(
                        channelType = PlayWidgetChannelType.Live,
                        totalView = "3rb",
                        coverUrl = "https://images.tokopedia.net/img/jJtrdn/2023/8/11/07b2f8e5-74b0-4860-9093-cfe3d3cc2570.jpg?b=UVDnp%5DajJ6ads%3Dn%25W%2CbH0cWBnQoMJ4V%5BjbWV"
                    ),
                    dummyChannel(
                        channelType = PlayWidgetChannelType.Live,
                        totalView = "99jt",
                        coverUrl = "https://images.tokopedia.net/img/jJtrdn/2023/8/11/15aaa572-f063-4393-bc85-7af09b501f2a.jpg?b=UWQu_%2BayofWB_NWCs.bbwXoLoJofI%5EofS5i_"
                    ),
                    dummyChannel(
                        channelType = PlayWidgetChannelType.Vod,
                        totalView = "14rb",
                        coverUrl = "https://images.tokopedia.net/img/generator/LScDrk/fa4a124180d351094048c06005160fcb.jpg"
                    ),
                    dummyChannel(
                        channelType = PlayWidgetChannelType.Vod,
                        totalView = "8rb",
                        coverUrl = "https://images.tokopedia.net/img/jJtrdn/2023/8/11/3e65f0ee-ffbd-4123-87df-d4e75f2a6835.jpg?b=UXLEE8~VkBNF~pM%7BNHxuR4NGR%2At6nhs%3BWCRi"
                    )
                )
            )
        )
    }

    private fun dummyChannel(
        channelType: PlayWidgetChannelType,
        totalView: String,
        coverUrl: String
    ): PlayWidgetItemUiModel {
        return PlayWidgetChannelUiModel(
            channelId = "1",
            title = "",
            appLink = "",
            startTime = "",
            totalView = PlayWidgetTotalView(totalView, true),
            promoType = PlayWidgetPromoType.NoPromo,
            reminderType = PlayWidgetReminderType.NotReminded,
            partner = PlayWidgetPartnerUiModel(
                "",
                "",
                PartnerType.Unknown,
                "",
                "",
                ""
            ),
            video = PlayWidgetVideoUiModel(
                "",
                false,
                coverUrl,
                ""
            ),
            channelType = channelType,
            hasGame = false,
            share = PlayWidgetShareUiModel("", false),
            performanceSummaryLink = "",
            poolType = "",
            recommendationType = "",
            hasAction = false,
            products = emptyList(),
            shouldShowPerformanceDashboard = false,
            channelTypeTransition = PlayWidgetChannelTypeTransition(
                prevType = null,
                currentType = PlayWidgetChannelType.Unknown
            ),
            gridType = PlayGridType.Unknown,
            extras = emptyMap()
        )
    }
}
