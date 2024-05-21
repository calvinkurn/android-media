package com.tokopedia.stories.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.report_content.model.UserReportOptions
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.types.TrackContentType
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.content.common.usecase.GetUserReportListUseCase
import com.tokopedia.content.common.usecase.PostUserReportUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.stories.data.mapper.StoriesMapperImpl
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.domain.usecase.StoriesDetailsUseCase
import com.tokopedia.stories.domain.usecase.StoriesTrackActivityUseCase
import com.tokopedia.stories.internal.StoriesPreferenceUtil
import com.tokopedia.stories.internal.storage.StoriesSeenStorage
import com.tokopedia.stories.internal.usecase.StoriesGroupsUseCase
import com.tokopedia.stories.uimodel.AuthorType
import com.tokopedia.stories.uimodel.StoryActionType
import com.tokopedia.stories.usecase.ProductMapper
import com.tokopedia.stories.usecase.StoriesProductUseCase
import com.tokopedia.stories.usecase.UpdateStoryUseCase
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.viewmodel.state.ProductBottomSheetUiState
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val storiesGroupsUseCase: StoriesGroupsUseCase,
    private val storiesDetailsUseCase: StoriesDetailsUseCase,
    private val storiesTrackActivityUseCase: StoriesTrackActivityUseCase,
    private val updateStoryUseCase: UpdateStoryUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val storiesProductUseCase: StoriesProductUseCase,
    private val productMapper: ProductMapper,
    private val userSession: UserSessionInterface,
    private val mapper: StoriesMapperImpl,
    private val seenStorage: StoriesSeenStorage,
    private val storiesPrefUtil: StoriesPreferenceUtil,
    private val getReportUseCase: GetUserReportListUseCase,
    private val postReportUseCase: PostUserReportUseCase,
    private val broadcasterReportTrackViewerUseCase: BroadcasterReportTrackViewerUseCase
) : StoriesRepository {

    override suspend fun getStoriesInitialData(
        authorId: String,
        authorType: String,
        source: String,
        sourceId: String,
        entryPoint: String,
        categoryIds: List<String>,
        productIds: List<String>
    ): StoriesUiModel =
        withContext(dispatchers.io) {
            val groupRequest = async {
                storiesGroupsUseCase(
                    StoriesGroupsUseCase.Request(
                        authorID = authorId,
                        authorType = authorType,
                        source = source,
                        sourceID = sourceId,
                        entryPoint = entryPoint,
                        categoryIds = categoryIds,
                        productIds = productIds
                    )
                )
            }
            val detailRequest = async {
                storiesDetailsUseCase(
                    StoriesDetailsUseCase.Request(
                        authorID = authorId,
                        authorType = authorType,
                        source = source,
                        sourceID = sourceId,
                        entryPoint = entryPoint,
                        categoryIds = categoryIds,
                        productIds = productIds
                    )
                )
            }
            val groupResult = groupRequest.await()
            val detailResult = detailRequest.await()
            return@withContext mapper.mapStoriesInitialData(groupResult, detailResult)
        }

    override suspend fun getStoriesDetailData(
        authorId: String,
        authorType: String,
        source: String,
        sourceId: String,
        entryPoint: String,
        categoryIds: List<String>,
        productIds: List<String>
    ): StoriesDetail =
        withContext(dispatchers.io) {
            val detailRequest = storiesDetailsUseCase(
                StoriesDetailsUseCase.Request(
                    authorID = authorId,
                    authorType = authorType,
                    source = source,
                    sourceID = sourceId,
                    entryPoint = entryPoint,
                    categoryIds = categoryIds,
                    productIds = productIds
                )
            )
            return@withContext mapper.mapStoriesDetailRequest("", detailRequest)
        }

    override suspend fun setStoriesTrackActivity(data: StoriesTrackActivityRequestModel): Boolean =
        withContext(dispatchers.io) {
            val trackActivityRequest = storiesTrackActivityUseCase(data)
            return@withContext trackActivityRequest.data.isSuccess
        }

    override suspend fun deleteStory(storyId: String): Boolean = withContext(dispatchers.io) {
        val param = UpdateStoryUseCase.Param(storyId, StoryActionType.Delete)
        val response = updateStoryUseCase(param)
        response.storyId.storyId == storyId
    }

    override suspend fun setHasSeenAllStories(
        authorId: String,
        authorType: AuthorType
    ) = withContext(dispatchers.main) {
        val author = when (authorType) {
            AuthorType.Seller -> StoriesSeenStorage.Author.Shop(authorId)
            AuthorType.User -> StoriesSeenStorage.Author.User(authorId)
            else -> null
        } ?: return@withContext

        seenStorage.setSeenAllAuthorStories(author)
    }

    override suspend fun setHasAckStoriesFeature() {
        storiesPrefUtil.setHasAckStoriesFeature()
    }

    override suspend fun setHasSeenManualStoriesDurationCoachmark() {
        storiesPrefUtil.setHasAckManualStoriesDuration()
    }

    override suspend fun hasSeenManualStoriesDurationCoachmark() =
        storiesPrefUtil.hasAckManualStoriesDuration()

    override suspend fun getStoriesProducts(
        shopId: String,
        storyId: String,
        catName: String
    ): ProductBottomSheetUiState {
        return withContext(dispatchers.io) {
            val response = storiesProductUseCase(
                storiesProductUseCase.convertToMap(
                    StoriesProductUseCase.Param(
                        id = storyId,
                        catName = catName
                    )
                )
            )
            ProductBottomSheetUiState(
                products = productMapper.mapProducts(response.data, shopId),
                campaign = productMapper.mapCampaign(response.data.campaign),
                resultState = ResultState.Success
            )
        }
    }

    override suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean {
        return withContext(dispatchers.io) {
            val response = addToCartUseCase.apply {
                setParams(
                    AddToCartUseCase.getMinimumParams(
                        productId = productId,
                        shopId = shopId,
                        atcExternalSource = AtcFromExternalSource.ATC_FROM_STORIES,
                        productName = productName,
                        price = price.toString(),
                        userId = userSession.userId
                    )
                )
            }.executeOnBackground()
            !response.isStatusError()
        }
    }

    override suspend fun getReportReasonList(): List<PlayUserReportReasoningUiModel.Reasoning> =
        withContext(dispatchers.io) {
            val response = getReportUseCase.executeOnBackground()
            response.data.map { reasoning ->
                PlayUserReportReasoningUiModel.Reasoning(
                    reasoningId = reasoning.id,
                    title = reasoning.value,
                    detail = reasoning.detail,
                    submissionData = if (reasoning.additionalField.isNotEmpty()) reasoning.additionalField.first() else UserReportOptions.OptionAdditionalField()
                )
            }
        }

    override suspend fun submitReport(
        storyDetail: StoriesDetailItem,
        reasonId: Int,
        timestamp: Long,
        reportDesc: String
    ): Boolean =
        withContext(dispatchers.io) {
            val source = when {
                TokopediaUrl.getInstance().TYPE == Env.STAGING && storyDetail.content.type == StoriesDetailItem.StoriesItemContentType.Image -> PostUserReportUseCase.ReportSource.STORY_STAGING_IMAGE
                storyDetail.content.type == StoriesDetailItem.StoriesItemContentType.Image -> PostUserReportUseCase.ReportSource.STORY_PROD_IMAGE
                storyDetail.content.type == StoriesDetailItem.StoriesItemContentType.Video -> PostUserReportUseCase.ReportSource.STORY_VIDEO
                else -> PostUserReportUseCase.ReportSource.UNKNOWN
            }

            val request = postReportUseCase.createParam(
                channelId = storyDetail.id.toLongOrZero(),
                mediaUrl = storyDetail.content.data,
                reasonId = reasonId,
                timestamp = timestamp,
                reportDesc = reportDesc,
                partnerId = storyDetail.author.id.toLongOrZero(),
                partnerType = PostUserReportUseCase.PartnerType.getTypeValue(storyDetail.author.type.value),
                reporterId = userSession.userId.toLongOrZero(),
                source = source
            )

            postReportUseCase.setRequestParams(request.parameters)
            val response = postReportUseCase.executeOnBackground()

            response.submissionReport.status.equals("success", true)
        }

    override suspend fun trackContent(storyId: String, productIds: List<String>, event: BroadcasterReportTrackViewerUseCase.Companion.Event) {
        withContext(dispatchers.io) {
            broadcasterReportTrackViewerUseCase.apply {
                params = BroadcasterReportTrackViewerUseCase.createParams(
                    channelId = storyId,
                    productIds = productIds,
                    event = event,
                    type = TrackContentType.Stories
                )
            }.executeOnBackground()
        }
    }
}
