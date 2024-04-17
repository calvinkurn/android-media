package com.tokopedia.feedplus.browse.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import com.tokopedia.feedplus.browse.data.model.StoryGroupsModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.domain.usecase.GetContentWidgetRecommendationUseCase
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import com.tokopedia.stories.internal.storage.StoriesSeenStorage
import com.tokopedia.stories.internal.usecase.StoriesGroupsUseCase
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
    private val storiesGroupsUseCase: StoriesGroupsUseCase,
    private val storiesSeenStorage: StoriesSeenStorage,
    private val mapper: FeedBrowseMapper,
    private val connectionUtil: PlayWidgetConnectionUtil,
    private val dispatchers: CoroutineDispatchers
) : FeedBrowseRepository {

    override suspend fun getHeaderDetail(): HeaderDetailModel? {
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
                mapper.mapHeaderData(response)
            } catch (_: Throwable) {
                null
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
                                "sourcesRequestType" to FEEDXHEADER_CATEGORY_INSPIRATION_PAGENAME,
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
            mapper.mapSlotsResponse(response).ifEmpty {
                error("no slots available")
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
                    isWifi = isWifi,
                    searchKeyword = extraParam.searchKeyword,
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

    override suspend fun getStoryGroups(
        source: String,
        cursor: String
    ): StoryGroupsModel = withContext(dispatchers.io) {
        val response = storiesGroupsUseCase(
            StoriesGroupsUseCase.Request(
                authorID = "",
                authorType = "",
                source = source,
                sourceID = "",
                entryPoint = "browse-page",
                cursor = cursor
            )
        )

        mapper.mapWidgetResponse(response)
    }

    override suspend fun getUpdatedSeenStoriesStatus(
        shopId: String,
        currentHasSeenAll: Boolean,
        lastUpdated: Long
    ): Boolean = withContext(dispatchers.io) {
        storiesSeenStorage.hasSeenAllAuthorStories(
            key = StoriesSeenStorage.Author.Shop(shopId),
            currentHasSeenAll = currentHasSeenAll,
            laterThanMillis = lastUpdated
        )
    }

    companion object {
        private const val FEEDXHEADER_CATEGORY_INSPIRATION_PAGENAME = "cdp-pagename"
    }
}
