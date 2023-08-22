package com.tokopedia.stories.data

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesUiModel

interface StoriesRepository {

    fun getStoriesData(): StoriesUiModel

    suspend fun getStoriesProducts(shopId: String, cursor: String) : List<ContentTaggedProductUiModel>

}
