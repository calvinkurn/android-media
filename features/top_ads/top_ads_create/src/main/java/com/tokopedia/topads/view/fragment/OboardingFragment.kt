package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.activity.StepperActivity
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.*

/**
 * Author errysuprayogi on 08,November,2019
 */

private const val CLICK_MULAI_IKLAN = "click-mulai iklan otomatis"
private const val CLICK_BUAT_IKLAN_MANUAL = "click-buat iklan manual"
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
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_MULAI_IKLAN, "")
            RouteManager.route(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
        }
        btn_start_manual_ads.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUAT_IKLAN_MANUAL, "")
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
