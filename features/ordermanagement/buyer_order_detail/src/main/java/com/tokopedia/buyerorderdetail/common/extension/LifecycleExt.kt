package com.tokopedia.buyerorderdetail.common.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

inline fun <T> LifecycleOwner.collectLatestWhenResumed(flow: Flow<T>, crossinline block: suspend (T) -> Unit) {
    lifecycleScope.launchWhenResumed { flow.collectLatest { block(it) } }
}
