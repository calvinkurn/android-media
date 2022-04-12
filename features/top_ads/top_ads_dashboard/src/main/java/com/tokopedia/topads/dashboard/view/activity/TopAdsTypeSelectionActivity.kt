package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.AUTO_ADS_DISABLED
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

private const val click_iklan_toko = "click - iklan toko"

class TopAdsTypeSelectionActivity : BaseSimpleActivity() {

    private lateinit var userSession: UserSessionInterface

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_ad_type_selection
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(this)
        findViewById<CardUnify>(R.id.topads_ad_card1).run {
            findViewById<View>(R.id.card_bg)?.background =
                AppCompatResources.getDrawable(context, R.drawable.ic_topads_added_ads_bg)
            findViewById<ImageUnify>(R.id.card_icon)?.setImageDrawable(context.getResDrawable(R.drawable.ic_topads_added_ads_produk))
            findViewById<Typography>(R.id.card_title)?.text =
                getString(R.string.topads_dashboard_ad_product_type_selection_title)
            findViewById<Typography>(R.id.card_subtitle).text =
                getString(R.string.topads_dashboard_ad_product_type_selection_subtitle)
            setOnClickListener {
                val intent = RouteManager.getIntent(this@TopAdsTypeSelectionActivity,
                    ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER)
                startActivityForResult(intent, AUTO_ADS_DISABLED)
            }
        }
        findViewById<CardUnify>(R.id.topads_ad_card2).run {
            findViewById<View>(R.id.card_bg)?.background =
                AppCompatResources.getDrawable(context, R.drawable.ic_topads_added_ads_bg)
            findViewById<ImageUnify>(R.id.card_icon)?.setImageDrawable(context.getResDrawable(R.drawable.ic_topads_added_ads_headline))
            findViewById<Typography>(R.id.card_title)?.text =
                getString(R.string.topads_dashboard_ad_headline_type_selection_title)
            findViewById<Typography>(R.id.card_subtitle)?.text =
                getString(R.string.topads_dashboard_ad_headline_type_selection_subtitle)
            setOnClickListener {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormClickEvent(
                    click_iklan_toko,
                    "{${userSession.shopId}}",
                    userSession.userId)
                RouteManager.route(this@TopAdsTypeSelectionActivity,
                    ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTO_ADS_DISABLED) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}