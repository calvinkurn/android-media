package com.tokopedia.analytics.performance.fpi.agregator

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * Created by yovi.putra on 06/07/23"
 * Project name: android-tokopedia-core
 **/

/**
 * Method 2 for measure frame performance
 */
abstract class BaseFpiAggregatorFragment : BaseDaggerFragment() {
    private val visibilityChangedListeners = ArrayList<OnHiddenChangedListener>()

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        visibilityChangedListeners.forEach { it.onHidden(hidden) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerTracer()
    }

    override fun onDestroy() {
        super.onDestroy()
        visibilityChangedListeners.clear()
    }

    fun addOnHiddenChangedListener(l: OnHiddenChangedListener) {
        visibilityChangedListeners.add(l)
    }

    fun removeOnHiddenChangedListener(l: OnHiddenChangedListener) {
        visibilityChangedListeners.remove(l)
    }
}
