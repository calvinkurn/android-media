package com.tokopedia.stories.data.repository

import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.usecase.PostUserReportUseCase
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.viewmodel.state.ProductBottomSheetUiState

interface StoriesRepository {

    suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesUiModel

    suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetail

    suspend fun setStoriesTrackActivity(data: StoriesTrackActivityRequestModel): Boolean

    suspend fun deleteStory(storyId: String): Boolean

    suspend fun setHasSeenAllStories(authorId: String, authorType: String)

    suspend fun setHasAckStoriesFeature()

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
        channelId: Long,
        mediaUrl: String,
        reasonId: Int,
        timestamp: Long,
        reportDesc: String,
        partnerId: Long,
        partnerType: PostUserReportUseCase.PartnerType,
        reporterId: Long
    ): Boolean
}
