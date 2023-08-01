package com.tokopedia.stories.data

import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor() : StoriesRepository {

    override fun getStoriesData(): String {
        return "data asli sumpah"
    }

}
