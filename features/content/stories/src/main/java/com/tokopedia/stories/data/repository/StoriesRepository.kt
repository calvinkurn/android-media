package com.tokopedia.stories.data.repository

import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.uimodel.AuthorType
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.viewmodel.state.ProductBottomSheetUiState

interface StoriesRepository {

    suspend fun getStoriesInitialData(
        authorId: String,
        authorType: String,
        source: String,
        sourceId: String,
        entryPoint: String,
        categoryIds: List<String>,
        productIds: List<String>
    ): StoriesUiModel

    suspend fun getStoriesDetailData(
        authorId: String,
        authorType: String,
        source: String,
        sourceId: String,
        entryPoint: String,
        categoryIds: List<String>,
        productIds: List<String>
    ): StoriesDetail

    suspend fun setStoriesTrackActivity(data: StoriesTrackActivityRequestModel): Boolean

    suspend fun deleteStory(storyId: String): Boolean

    suspend fun setHasSeenAllStories(authorId: String, authorType: AuthorType)

    suspend fun setHasAckStoriesFeature()

    suspend fun setHasSeenManualStoriesDurationCoachmark()

    suspend fun hasSeenManualStoriesDurationCoachmark(): Boolean

    suspend fun getStoriesProducts(
        shopId: String,
        storyId: String,
        catName: String
    ): ProductBottomSheetUiState

    suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean

    suspend fun getReportReasonList(): List<PlayUserReportReasoningUiModel.Reasoning>

    suspend fun submitReport(
        storyDetail: StoriesDetailItem,
        reasonId: Int,
        timestamp: Long,
        reportDesc: String
    ): Boolean

    suspend fun trackContent(
        storyId: String,
        productIds: List<String>,
        event: BroadcasterReportTrackViewerUseCase.Companion.Event
    )
}
