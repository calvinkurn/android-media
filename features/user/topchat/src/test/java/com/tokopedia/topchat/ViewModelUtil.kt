package com.tokopedia.topchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

/**
 * Will create new [ViewModelStore], add view model into it using [ViewModelProvider]
 * and then call [ViewModelStore.clear], that will cause [ViewModel.onCleared] to be called
 */
fun ViewModel.callOnCleared() {
    val viewModelStore = ViewModelStore()
    val viewModelProvider = ViewModelProvider(
            viewModelStore,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(
                        modelClass: Class<T>
                ): T = this@callOnCleared as T
            }
    )
    viewModelProvider.get(this@callOnCleared::class.java)
    viewModelStore.clear()
}