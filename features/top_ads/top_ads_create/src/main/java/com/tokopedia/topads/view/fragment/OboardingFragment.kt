package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
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
            RouteManager.getIntent(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE).apply {
                if (isFromPdpSellerMigration()) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName())
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks())
                }
                startActivity(this)
            }
        }
        btn_start_manual_ads.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUAT_IKLAN_MANUAL, "")
            Intent(activity, StepperActivity::class.java).apply {
                if (isFromPdpSellerMigration()) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName())
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks())
                }
                startActivity(this)
            }
        }
    }

    private fun getSellerMigrationRedirectionApplinks(): ArrayList<String> {
        return ArrayList(activity?.intent?.getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA).orEmpty())
    }

    private fun getSellerMigrationFeatureName(): String {
        return activity?.intent?.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).orEmpty()
    }

    private fun isFromPdpSellerMigration(): Boolean {
        return !activity?.intent?.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).isNullOrEmpty()
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
