package com.tokopedia.topads.auto.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.router.TopAdsAutoRouter

/**
 * Author errysuprayogi on 07,May,2019
 */
class AutoAdsActivatedFragment : BaseDaggerFragment() {

    private var performanceBtn: Button? = null

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_autoads_activated, container, false)
        performanceBtn = view.findViewById(R.id.see_performance_btn)
        performanceBtn!!.setOnClickListener {
            (activity!!.application as TopAdsAutoRouter).openTopAdsDashboardApplink(activity!!)
            activity!!.finishAffinity()
        }
        return view
    }

    companion object {

        fun newInstance(): AutoAdsActivatedFragment {

            val args = Bundle()

            val fragment = AutoAdsActivatedFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
