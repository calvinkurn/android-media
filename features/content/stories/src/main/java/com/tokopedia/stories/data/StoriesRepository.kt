package com.tokopedia.stories.data

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.stories.view.model.StoriesUiModel

interface StoriesRepository {

    fun getStoriesData(): StoriesUiModel

    suspend fun getStoriesProducts(shopId: String, cursor: String) : List<ContentTaggedProductUiModel>

    suspend fun addToCart(productId: String, productName: String, shopId: String, price: Double) : Boolean

    suspend fun getMvcWidget(shopId: String) : TokopointsCatalogMVCSummaryResponse

}
