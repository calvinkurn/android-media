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

    override suspend fun getCategoryInspirationTitle(source: String): String {
        return withContext(dispatchers.io) {
            try {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        listOf(
                            FeedXHeaderRequestFields.DETAIL.value
                        ),
                        listOf(
                            mapOf(
                                "sourcesRequestType" to "cdp-pagename",
                                "sourcesRequestID" to source
                            )
                        )
                    )
                )
                val response = feedXHeaderUseCase.executeOnBackground()
                response.feedXHeaderData.data.detail.title
            } catch (_: Throwable) {
                ""
            }
        }
    }

    override suspend fun getSlots(): List<FeedBrowseSlotUiModel> {
        return withContext(dispatchers.io) {
            val response = feedXHomeUseCase(
                feedXHomeUseCase.createParams(source = FeedXHomeUseCase.SOURCE_BROWSE)
            )
            // TODO("Remove this mock data")
            listOf(
                FeedBrowseSlotUiModel.InspirationBanner(
                    slotId = "item.id",
                    title = "Ini Banner",
                    identifier = "content_browse_inspirational",
                    bannerList = emptyList()
                ),
                FeedBrowseSlotUiModel.Authors(
                    slotId = "random.slot.id",
                    title = "Video asik dari kreator ",
                    identifier = "content_browse_ugc",
                    authorList = emptyList()
                )
            ) + mapper.mapSlotsResponse(response).ifEmpty {
                throw IllegalStateException("no slots available")
            }
        }
    }

    override suspend fun getCategoryInspirationTemplate(
        source: String,
        entryPoint: String
    ): List<FeedBrowseSlotUiModel> {
        return withContext(dispatchers.io) {
            val response = feedXHomeUseCase(
                feedXHomeUseCase.createParams(source = source, entryPoint = entryPoint)
            )
            mapper.mapSlotsResponse(response)
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
}
