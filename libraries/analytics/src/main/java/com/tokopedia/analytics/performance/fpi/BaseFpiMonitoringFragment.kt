package com.tokopedia.analytics.performance.fpi

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * Created by yovi.putra on 14/07/23"
 * Project name: android-tokopedia-core
 **/


abstract class BaseFpiMonitoringFragment : BaseDaggerFragment() {

    private val fpiMonitoring: FpiMonitoringDelegate by lazy { FpiMonitoringDelegateImpl() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fpiMonitoring.onViewCreated(
            pageName = getFpiPageName(),
            fragment = this
        )
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        fpiMonitoring.onHiddenChanged(hidden)
    }

    fun getFrameMetric() = fpiMonitoring.getFrameMetric()

    abstract fun getFpiPageName(): String
}
