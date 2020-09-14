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
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.activity.StepperActivity
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.*
import kotlinx.android.synthetic.main.topads_create_fragment_onboarding.view.*

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
        view.imageView7.setImageDrawable(view.context.getResDrawable(R.drawable.ill_header))
        view.top_corner_bg1.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_bg_top_corner))
        view.icon1.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_thumb_up))
        view.icon2.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_iklan_otomatis_1))
        view.icon3.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_iklan_otomatis_2))
        view.icon4.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_iklan_otomatis_3))

        view.top_corner_bg2.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_bg_top_corner))
        view.icon5.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_gear))
        view.icon6.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_iklan_manual_1))
        view.icon7.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_iklan_manual_2))
        view.icon8.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_iklan_manual_3))


        btn_start_auto_ads.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_MULAI_IKLAN, "")
            RouteManager.getIntent(it.context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE).apply {
                if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                }
                startActivity(this)
            }
        }
        btn_start_manual_ads.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUAT_IKLAN_MANUAL, "")
            Intent(activity, StepperActivity::class.java).apply {
                if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                }
                startActivity(this)
            }
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
