package com.tokopedia.stories.data.repository

import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.view.model.ProductBottomSheetUiState
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesUiModel

interface StoriesRepository {

    suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesUiModel

    suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetail

    suspend fun setStoriesTrackActivity(data: StoriesTrackActivityRequestModel): Boolean

    suspend fun deleteStory(storyId: String) : Boolean

    suspend fun getStoriesProducts(shopId: String, storyId: String, catName: String) : ProductBottomSheetUiState

    suspend fun addToCart(productId: String, productName: String, shopId: String, price: Double) : Boolean
}
