package com.tokopedia.stories.data.repository

import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.view.model.ProductBottomSheetUiState
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel

interface StoriesRepository {

    suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesGroupUiModel

    suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetailUiModel

    suspend fun deleteStory(storyId: String) : Boolean

    suspend fun getStoriesProducts(shopId: String, storyId: String) : ProductBottomSheetUiState

    suspend fun addToCart(productId: String, productName: String, shopId: String, price: Double) : Boolean
}
