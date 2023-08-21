package com.tokopedia.stories.data

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesUiModel

interface StoriesRepository {

    fun getStoriesData(): StoriesUiModel

    fun getStoriesProducts() : List<ContentTaggedProductUiModel> //TODO() will handle pagination

}
