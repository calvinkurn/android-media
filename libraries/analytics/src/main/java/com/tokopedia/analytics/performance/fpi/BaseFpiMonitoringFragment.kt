package com.tokopedia.analytics.performance.fpi

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig

/**
 * Created by yovi.putra on 14/07/23"
 * Project name: android-tokopedia-core
 **/


abstract class BaseFpiMonitoringFragment : BaseDaggerFragment() {

    private val fpiMonitoring: FpiMonitoringDelegate by lazy { FpiMonitoringDelegateImpl() }

    // ref to DeveloperOptionActivity.PREF_KEY_FPI_MONITORING_POPUP
    private val prefKey = "fpi_monitoring_popup"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(isActive()) fpiMonitoring.onViewCreated(this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (isActive()) fpiMonitoring.onHiddenChanged(hidden)
    }

    private fun isActive(): Boolean {
        val context = context ?: return false
        return GlobalConfig.DEBUG && context.isFpiMonitoringEnable()
    }

    private fun Context.isFpiMonitoringEnable(): Boolean = getSharedPreferences(
        prefKey, BaseActivity.MODE_PRIVATE
    ).getBoolean(prefKey, false)
}
