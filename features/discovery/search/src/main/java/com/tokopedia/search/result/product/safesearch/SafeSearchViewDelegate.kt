package com.tokopedia.search.result.product.safesearch

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import java.lang.ref.Reference
import java.lang.ref.WeakReference

internal class SafeSearchViewDelegate(
    lifecycleOwner: LifecycleOwner?
) : SafeSearchView {
    private val lifecycleOwnerReference: Reference<LifecycleOwner> = WeakReference(lifecycleOwner)
    private val lifecycleOwner: LifecycleOwner?
        get() = lifecycleOwnerReference.get()
    private val processLifecycleOwner: LifecycleOwner
        get() = ProcessLifecycleOwner.get()

    override fun registerSameSessionListener(safeSearchPreference: MutableSafeSearchPreference) {
        addAppLifecycleObserver(safeSearchPreference)
        addActivityLifecycleObserver(safeSearchPreference)
    }

    private fun addAppLifecycleObserver(safeSearchPreference: MutableSafeSearchPreference) {
        val lifecycleObserver = SafeSearchAppLifecycleObserver(safeSearchPreference)
        processLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
    }

    private fun addActivityLifecycleObserver(
        safeSearchPreference: MutableSafeSearchPreference,
    ) {
        val activityLifecycleObserver = SafeSearchActivityLifecycleObserver(safeSearchPreference)
        lifecycleOwner?.lifecycle?.addObserver(activityLifecycleObserver)
    }

    override fun release() {
        lifecycleOwnerReference.clear()
    }
}
