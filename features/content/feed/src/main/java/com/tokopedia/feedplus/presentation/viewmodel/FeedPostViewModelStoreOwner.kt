package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * Created by kenny.hadisaputra on 24/11/23
 */
class FeedPostViewModelStoreOwner(
    private val provider: FeedPostViewModelStoreProvider,
    private val key: String
) : ViewModelStoreOwner {

    override fun getViewModelStore(): ViewModelStore {
        return provider.getOrCreateViewModelStore(key)
    }
}
