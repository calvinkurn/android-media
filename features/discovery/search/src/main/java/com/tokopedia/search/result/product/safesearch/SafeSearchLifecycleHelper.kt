package com.tokopedia.search.result.product.safesearch

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

object SafeSearchLifecycleHelper {

    private var activityLifecycleOwner: LifecycleOwner? = null
    private var activityLifecycleObserver: LifecycleObserver? = null

    private var processLifecycleObserver: LifecycleObserver? = null
    private val processLifecycleOwner: LifecycleOwner
        get() = ProcessLifecycleOwner.get()

    fun resetIfNoObserver(
        safeSearchPreference: SafeSearchPreference,
    ) = synchronized(this) {
        if(this.processLifecycleObserver != null) return
        safeSearchPreference.isShowAdult = false
    }

    fun registerProcessLifecycleOwner(
        safeSearchPreference: SafeSearchPreference,
        lifecycleOwner: LifecycleOwner?,
    ) = synchronized(this) {
        if (this.processLifecycleObserver != null) return
        addAppLifecycleObserver(safeSearchPreference)
        addActivityLifecycleObserver(safeSearchPreference, lifecycleOwner)
    }

    private fun addAppLifecycleObserver(safeSearchPreference: SafeSearchPreference) {
        val lifecycleObserver = SafeSearchAppLifecycleObserver(safeSearchPreference)
        processLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        this.processLifecycleObserver = lifecycleObserver
    }

    private fun addActivityLifecycleObserver(
        safeSearchPreference: SafeSearchPreference,
        lifecycleOwner: LifecycleOwner?,
    ) {
        val activityLifecycleObserver = SafeSearchActivityLifecycleObserver(safeSearchPreference)
        lifecycleOwner?.lifecycle?.addObserver(activityLifecycleObserver)
        this.activityLifecycleOwner = lifecycleOwner
        this.activityLifecycleObserver = activityLifecycleObserver
    }

    internal fun unregisterLifecycleObserver() = synchronized(this) {
        removeProcessLifecycleObserver()
        removeActivityLifecycleObserver()
    }

    private fun removeProcessLifecycleObserver() {
        val lifecycleObserver = this.processLifecycleObserver ?: return
        processLifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        this.processLifecycleObserver = null
    }

    private fun removeActivityLifecycleObserver() {
        val lifecycleObserver = this.activityLifecycleObserver ?: return
        activityLifecycleOwner?.lifecycle?.removeObserver(lifecycleObserver)
        this.activityLifecycleObserver = null
        this.activityLifecycleOwner = null
    }
}
