package com.tokopedia.topads.auto.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.view.activity.DailyBudgetActivity
import com.tokopedia.topads.auto.view.activity.EmptyProductActivity
import com.tokopedia.topads.auto.view.activity.InsufficientBalanceActivity

/**
 * Author errysuprayogi on 07,May,2019
 */
class StartAutoAdsFragment : BaseDaggerFragment() {

    private var btnStart: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_new_ads_onboarding, container, false)
        btnStart = view.findViewById(R.id.btn_start)
        btnStart!!.setOnClickListener { startActivity(Intent(activity, DailyBudgetActivity::class.java)) }
        return view
    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    companion object {

        fun newInstance(): StartAutoAdsFragment {

            val args = Bundle()

            val fragment = StartAutoAdsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
