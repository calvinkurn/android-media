package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

/**
 * Created by kenny.hadisaputra on 24/11/23
 */
class FeedPostViewModelStoreProvider : ViewModel() {

    private val viewModelStoreMap = mutableMapOf<String, ViewModelStore>()

    fun getOrCreateViewModelStore(key: String): ViewModelStore {
        if (viewModelStoreMap.containsKey(key)) return viewModelStoreMap[key]!!
        return ViewModelStore().also {
            viewModelStoreMap[key] = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelStoreMap.forEach { it.value.clear() }
        viewModelStoreMap.clear()
    }
}
