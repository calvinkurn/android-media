package com.tokopedia.stories.data

import com.tokopedia.stories.view.model.StoriesCategoriesUiModel
import com.tokopedia.stories.view.model.StoriesDataUiModel
import com.tokopedia.stories.view.model.StoriesUiModel
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor() : StoriesRepository {

    override fun getStoriesData(): StoriesUiModel {
        return StoriesUiModel(
            categories = listOf(
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 1",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 2",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 3",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 4",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 5",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 6",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 7",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 8",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 9",
                ),
                StoriesCategoriesUiModel(
                    image = "",
                    title = "Categories 10",
                ),
            ),
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
