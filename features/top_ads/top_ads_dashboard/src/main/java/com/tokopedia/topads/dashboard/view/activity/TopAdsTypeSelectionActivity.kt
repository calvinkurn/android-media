package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.AUTO_ADS_DISABLED
import kotlinx.android.synthetic.main.activity_ad_type_selection.*
import kotlinx.android.synthetic.main.item_topads_added_ads.view.*

class TopAdsTypeSelectionActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_ad_type_selection
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topads_ad_card1.run {
            card_bg.background = AppCompatResources.getDrawable(context, R.drawable.ic_topads_added_ads_bg)
            card_icon.setImageDrawable(context.getResDrawable(R.drawable.ic_topads_added_ads_produk))
            card_title.text = getString(R.string.topads_dashboard_ad_product_type_selection_title)
            card_subtitle.text = getString(R.string.topads_dashboard_ad_product_type_selection_subtitle)
            setOnClickListener {
                val intent = RouteManager.getIntent(this@TopAdsTypeSelectionActivity, ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER)
                startActivityForResult(intent, AUTO_ADS_DISABLED)
            }
        }
        topads_ad_card2.run {
            card_bg.background = AppCompatResources.getDrawable(context, R.drawable.ic_topads_added_ads_bg)
            card_icon.setImageDrawable(context.getResDrawable(R.drawable.ic_topads_added_ads_headline))
            card_title.text = getString(R.string.topads_dashboard_ad_headline_type_selection_title)
            card_subtitle.text = getString(R.string.topads_dashboard_ad_headline_type_selection_subtitle)
            setOnClickListener {
                RouteManager.route(this@TopAdsTypeSelectionActivity, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION)
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