package com.tokopedia.play_common.delegate

import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleObserver

/**
 * Created by jegul on 26/04/21
 */
class FragmentViewContainer() {

    private val viewMap = mutableMapOf<@IdRes Int, View>()

    private var viewLifecycleObserver: LifecycleObserver? = null

    internal fun getViewLifecycleObserver(): LifecycleObserver? = synchronized(this) {
        return viewLifecycleObserver
    }

    internal fun setViewLifecycleObserver(lifecycleObserver: LifecycleObserver) = synchronized(this) {
        this.viewLifecycleObserver = lifecycleObserver
    }

    internal fun addView(view: View) {
        viewMap[view.id] = view
    }

    internal fun clearViews() {
        viewMap.clear()
    }
}