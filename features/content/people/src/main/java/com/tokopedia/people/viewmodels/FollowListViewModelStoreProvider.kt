package com.tokopedia.people.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import com.tokopedia.people.views.uimodel.FollowListType

internal class FollowListViewModelStoreProvider : ViewModel() {

    private val viewModelStoreMap = mutableMapOf<FollowListType, ViewModelStore>()

    fun getOrCreateViewModelStore(type: FollowListType): ViewModelStore {
        if (viewModelStoreMap.containsKey(type)) return viewModelStoreMap[type]!!
        return ViewModelStore().also {
            viewModelStoreMap[type] = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelStoreMap.forEach { it.value.clear() }
        viewModelStoreMap.clear()
    }
}
