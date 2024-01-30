package com.tokopedia.feedplus.browse.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.StoryGroupsModel
import com.tokopedia.feedplus.browse.data.model.StoryNodeModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.domain.usecase.GetContentWidgetRecommendationUseCase
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.kotlin.extensions.view.isEven
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import com.tokopedia.stories.internal.storage.StoriesSeenStorage
import com.tokopedia.stories.internal.usecase.StoriesGroupsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

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
//            mapper.mapSlotsResponse(response).ifEmpty {
//                error("no slots available")
//            }

            listOf(
                FeedBrowseSlotUiModel.StoryGroups(
                    slotId = "story_slot",
                    title = "",
                    storyList = emptyList(),
                    nextCursor = "",
                    source = "browse-page"
                )
            ) + mapper.mapSlotsResponse(response).ifEmpty {
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

    override suspend fun getStoryGroups(
        source: String,
        cursor: String
    ): StoryGroupsModel = withContext(dispatchers.io) {
//        val response = storiesGroupsUseCase(
//            StoriesGroupsUseCase.Request(
//                authorID = "",
//                authorType = "",
//                source = source,
//                sourceID = "",
//                entryPoint = "browse-page",
//                cursor = cursor,
//            )
//        )
//
//        mapper.mapWidgetResponse(response)
        delay(1.5.seconds)

        StoryGroupsModel(
            List(20) {
                StoryNodeModel(
                    id = if (it == 0) "7240865" else "12751408",
                    name = "Story $it",
                    thumbnailUrl = "https://images.tokopedia.net/img/cache/100-square/tPxBYm/2023/8/1/0fc7d4e1-c812-4bbf-8e73-45ccb3661c84.jpg",
                    hasUnseenStory = it.isEven(),
                    appLink = if (it == 0) "tokopedia://stories/shop/7240865" else "tokopedia://stories/shop/12751408",
                    lastUpdatedAt = System.currentTimeMillis(),
                    authorType = AuthorType.Shop
                )
            },
            nextCursor = ""
        )
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
