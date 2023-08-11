package com.tokopedia.stories.data

import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.model.StoriesUiModel
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor() : StoriesRepository {

    override fun getStoriesData(): StoriesUiModel {
        return StoriesUiModel(
            group = listOf(
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                    title = "Group 1",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/10/26/21c778b5-e16b-42c5-a5ac-0c934f6b61f3.jpg",
                    title = "Group 2",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2020/10/8/7e471c19-c137-4d4d-b9b6-5ff79d1d959e.png",
                    title = "Group 3",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                    title = "Group 4",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/shops-1/2020/10/24/9684904/9684904_2a745e8b-179a-49d7-9d5c-c04554d1987b.jpg",
                    title = "Group 5",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                    title = "Group 6",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/9/16/e2345b9d-5b35-441e-95f1-26fde30458db.png",
                    title = "Group 7",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/2/9/11c96da3-c58d-47d8-8d01-b0b4b6f01364.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/2/9/11c96da3-c58d-47d8-8d01-b0b4b6f01364.jpg",
                    title = "Group 8",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/1/14/a997a92f-f222-4a1b-8a1b-52bd4dda1161.jpg",
                    title = "Group 9",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/10/26/21c778b5-e16b-42c5-a5ac-0c934f6b61f3.jpg",
                    title = "Group 10",
                    selectedStories = 0,
                    stories = listOf(
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            position = 1,
                            selected = false,
                            isPause = false,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                    ),
                ),
            )
        )
    }

}
