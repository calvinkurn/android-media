package com.tokopedia.stories.data

import com.tokopedia.stories.view.model.StoriesDataUiModel
import com.tokopedia.stories.view.model.StoriesUiModel
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor() : StoriesRepository {

    override fun getStoriesData(): StoriesUiModel {
        return StoriesUiModel(
            stories = listOf(
                StoriesDataUiModel(
                    count = 2,
                    selected = 1,
                    isPause = false,
                ),
                StoriesDataUiModel(
                    count = 3,
                    selected = 1,
                    isPause = false,
                ),
                StoriesDataUiModel(
                    count = 5,
                    selected = 1,
                    isPause = false,
                ),
                StoriesDataUiModel(
                    count = 1,
                    selected = 1,
                    isPause = false,
                ),
                StoriesDataUiModel(
                    count = 2,
                    selected = 1,
                    isPause = false,
                ),
                StoriesDataUiModel(
                    count = 3,
                    selected = 1,
                    isPause = false,
                ),
            )
        )
    }

}
