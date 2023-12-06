package com.tokopedia.feedplus.browse.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.domain.usecase.GetContentWidgetRecommendationUseCase
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseRepositoryImpl @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val playWidgetSlotUseCase: GetPlayWidgetSlotUseCase,
    private val getContentWidgetRecommendationUseCase: GetContentWidgetRecommendationUseCase,
    private val mapper: FeedBrowseMapper,
    private val connectionUtil: PlayWidgetConnectionUtil,
    private val dispatchers: CoroutineDispatchers
) : FeedBrowseRepository {

    override suspend fun getTitle(): String {
        return withContext(dispatchers.io) {
            try {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        listOf(
                            FeedXHeaderRequestFields.BROWSE.value
                        )
                    )
                )
                val response = feedXHeaderUseCase.executeOnBackground()
                mapper.mapTitle(response)
            } catch (_: Throwable) {
                ""
            }
        }
    }

    override suspend fun getSlots(): List<FeedBrowseSlotUiModel> {
        return withContext(dispatchers.io) {
            val response = feedXHomeUseCase(
                feedXHomeUseCase.createParams(source = FeedXHomeUseCase.SOURCE_BROWSE, cursor = "TUN3d0xESXNNQ3dzTXl3c01Dd3dMREFzTERBc01Dd3NMQT09Og==")
            )
            // TODO("Remove this mock data")
            listOf(
                FeedBrowseSlotUiModel.InspirationBanner(
                    slotId = "item.id",
                    title = "Ini Banner",
                    identifier = "content_browse_inspirational",
                    bannerList = emptyList(),
                    isLoading = false,
                ),
                FeedBrowseSlotUiModel.Authors(
                    slotId = "random.slot.id",
                    title = "Video asik dari kreator ",
                    identifier = "content_browse_ugc",
                    authorList = emptyList(),
                    isLoading = false,
                )
            ) + mapper.mapSlotsResponse(response).ifEmpty {
                throw IllegalStateException("no slots available")
            }
        }
    }

    override suspend fun getWidgetContentSlot(
        extraParam: WidgetRequestModel
    ): ContentSlotModel = withContext(dispatchers.io) {
        val isWifi = connectionUtil.isEligibleForHeavyDataUsage()
        val response = playWidgetSlotUseCase(
            GetPlayWidgetSlotUseCase.Param(
                req = GetPlayWidgetSlotUseCase.Param.Request(
                    group = extraParam.group,
                    cursor = extraParam.cursor,
                    sourceId = extraParam.sourceId,
                    sourceType = extraParam.sourceType,
                    isWifi = isWifi
                )
            )
        )
        mapper.mapWidgetResponse(response)
    }

    override suspend fun getWidgetRecommendation(
        identifier: String
    ): WidgetRecommendationModel = withContext(dispatchers.io) {
        val response = getContentWidgetRecommendationUseCase(
            GetContentWidgetRecommendationUseCase.Param.create(identifier)
        )

        mapper.mapWidgetResponse(response)
    }

//    private fun mockCreators() = WidgetRecommendationModel.Authors(
//        channels = List(5) { index ->
//            PlayWidgetChannelUiModel(
//                channelId = "$index",
//                title = "",
//                appLink = "https://www.tokopedia.com/play/channel/1729780",
//                startTime = "",
//                totalView = PlayWidgetTotalView("2.9 rb", true),
//                promoType = PlayWidgetPromoType.NoPromo,
//                reminderType = PlayWidgetReminderType.NotReminded,
//                partner = PlayWidgetPartnerUiModel(
//                    id = "$index",
//                    name = "Wynne Armeline",
//                    avatarUrl = "https://images.tokopedia.net/img/cache/100-square/tPxBYm/2023/6/23/91d6baf5-b79e-45c1-bf58-9f033b14a69f.jpg",
//                    badgeUrl = "",
//                    appLink = "https://www.tokopedia.com/people/wynnearmeline",
//                    type = PartnerType.Buyer
//                ),
//                video = PlayWidgetVideoUiModel.Empty.copy(coverUrl = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/10/17/8ccb0444-a1b9-464c-b86f-119576b47504.jpg"),
//                channelType = PlayWidgetChannelType.Vod,
//                hasGame = false,
//                share = PlayWidgetShareUiModel.Empty,
//                performanceSummaryLink = "",
//                poolType = "",
//                recommendationType = "",
//                hasAction = false,
//                products = emptyList(),
//                shouldShowPerformanceDashboard = false,
//                channelTypeTransition = PlayWidgetChannelTypeTransition(
//                    prevType = null,
//                    currentType = PlayWidgetChannelType.Unknown
//                )
//            )
//        }
//    )
}
