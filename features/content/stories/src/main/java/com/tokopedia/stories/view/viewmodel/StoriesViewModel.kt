package com.tokopedia.stories.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tokopedia.stories.data.StoriesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StoriesViewModel @AssistedInject constructor(
    @Assisted private val handle: SavedStateHandle,
    private val repository: StoriesRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(handle: SavedStateHandle): StoriesViewModel
    }

    private var shopId = ""
    private var storiesId = ""

    fun getData(): String {
        return "$shopId - $storiesId"
    }

    fun saveInitialData(data: List<String>) {
        shopId = data[SHOP_ID_INDEX]
        storiesId = if (data.size > 2) data[STORIES_ID_INDEX] else ""
    }

    companion object {
        private const val SHOP_ID_INDEX = 1
        private const val STORIES_ID_INDEX = 2
    }

}
