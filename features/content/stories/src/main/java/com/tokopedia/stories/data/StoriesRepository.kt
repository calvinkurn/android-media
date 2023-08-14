package com.tokopedia.stories.data

import com.tokopedia.stories.view.model.StoriesUiModel

interface StoriesRepository {

    fun getStoriesData(): StoriesUiModel

}
