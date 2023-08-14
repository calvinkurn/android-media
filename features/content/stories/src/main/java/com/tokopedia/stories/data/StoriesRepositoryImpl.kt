package com.tokopedia.stories.data

import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel.StoriesDetailUiEvent
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.model.StoriesUiModel
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor() : StoriesRepository {

    override fun getStoriesData(): StoriesUiModel {
        return StoriesUiModel(
            selectedGroup = 0,
            groups = listOf(
                StoriesGroupUiModel(
                    id = "1",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                    title = "Group 1",
                    selectedDetail = 0,
                    selected = true,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "1",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "2",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "3",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "2",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/10/26/21c778b5-e16b-42c5-a5ac-0c934f6b61f3.jpg",
                    title = "Group 2",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "4",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "5",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "6",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            id = "7",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "8",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "9",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "3",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2020/10/8/7e471c19-c137-4d4d-b9b6-5ff79d1d959e.png",
                    title = "Group 3",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "10",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            id = "101",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "4",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                    title = "Group 4",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "11",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "12",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "13",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            id = "14",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "15",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "16",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "5",
                    image = "https://images.tokopedia.net/img/cache/215-square/shops-1/2020/10/24/9684904/9684904_2a745e8b-179a-49d7-9d5c-c04554d1987b.jpg",
                    title = "Group 5",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "17",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "18",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "6",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                    title = "Group 6",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "19",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "20",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "21",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            id = "22",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "7",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/9/16/e2345b9d-5b35-441e-95f1-26fde30458db.png",
                    title = "Group 7",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "23",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "24",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "25",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/2/9/11c96da3-c58d-47d8-8d01-b0b4b6f01364.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "8",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/2/9/11c96da3-c58d-47d8-8d01-b0b4b6f01364.jpg",
                    title = "Group 8",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "26",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "266",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "267",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "9",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/1/14/a997a92f-f222-4a1b-8a1b-52bd4dda1161.jpg",
                    title = "Group 9",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "27",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "27",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                    ),
                ),
                StoriesGroupUiModel(
                    id = "10",
                    image = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/10/26/21c778b5-e16b-42c5-a5ac-0c934f6b61f3.jpg",
                    title = "Group 10",
                    selectedDetail = 0,
                    selected = false,
                    details = listOf(
                        StoriesDetailUiModel(
                            id = "28",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "29",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "30",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/8/25/bc26363b-d20d-472b-9427-341c3d17d66e.png",
                        ),
                        StoriesDetailUiModel(
                            id = "31",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/12/30/e1ada065-5330-4952-940c-4ff17220a47f.jpg",
                        ),
                        StoriesDetailUiModel(
                            id = "32",
                            selected = 1,
                            event = StoriesDetailUiEvent.START,
                            imageContent = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/4/8/61592e1f-6c0d-408d-a535-57a4a6858bf0.jpg",
                        ),
                    ),
                ),
            )
        )
    }

}
