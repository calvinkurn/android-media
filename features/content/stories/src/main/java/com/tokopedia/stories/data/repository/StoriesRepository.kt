package com.tokopedia.stories.data.repository

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse

interface StoriesRepository {

    suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesGroupUiModel

    suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetailUiModel

    suspend fun deleteStory(storyId: String) : Boolean

    suspend fun getStoriesProducts(shopId: String, cursor: String) : List<ContentTaggedProductUiModel>

    suspend fun addToCart(productId: String, productName: String, shopId: String, price: Double) : Boolean

    suspend fun getMvcWidget(shopId: String) : TokopointsCatalogMVCSummaryResponse
}
