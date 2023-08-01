package com.tokopedia.stories.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.stories.data.StoriesRepository
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val repository: StoriesRepository,
) : ViewModel() {

    private var shopId = ""
    private var storiesId = ""

    fun getData(): String {
        return "$shopId - $storiesId"
    }

    fun saveInitialData(data: List<String>) {
        shopId = data[SHOP_ID_INDEX]
        storiesId = data[STORIES_ID_INDEX]
    }

    companion object {
        private const val SHOP_ID_INDEX = 1
        private const val STORIES_ID_INDEX = 2
    }

}
