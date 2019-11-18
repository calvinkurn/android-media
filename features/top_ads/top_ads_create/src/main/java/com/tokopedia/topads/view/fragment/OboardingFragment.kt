package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.activity.StepperActivity
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.*

/**
 * Author errysuprayogi on 08,November,2019
 */
class OboardingFragment: TkpdBaseV4Fragment() {

    override fun getScreenName(): String {
        return OboardingFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_start_auto_ads.setOnClickListener {
            RouteManager.route(it.context, ApplinkConst.SellerApp.TOPADS_AUTOADS)
        }
        btn_start_manual_ads.setOnClickListener {
            startActivity(Intent(activity, StepperActivity::class.java))
        }
    }
    companion object {

        fun newInstance(): OboardingFragment {
            val args = Bundle()
            val fragment = OboardingFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
