package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.AUTO_ADS_DISABLED
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.IKLAN_PRODUCT_AD_TYPE_IMG_URL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.IKLAN_TOKO_AD_TYPE_IMG_URL
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
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
            findViewById<Typography>(R.id.title)?.text =
                getString(R.string.topads_dash_iklan_produck)
            findViewById<Typography>(R.id.subtitle)?.text =
                getString(R.string.topads_ad_type_selection_product_subtitle)
            val accordianData = findViewById<Group>(R.id.accordian_group)
            findViewById<Typography>(R.id.accordian_text)?.run {
                text = getString(R.string.topads_ad_type_selection_product_accordian_title)
                setOnClickListener { expandAccordian(accordianData) }
            }
            findViewById<ImageUnify>(R.id.accordian_icon)?.setOnClickListener { expandAccordian(accordianData) }
            findViewById<Typography>(R.id.item1)?.text =
                getString(R.string.topads_ad_type_selection_product_accordian_item1)
            findViewById<Typography>(R.id.item2)?.text =
                getString(R.string.topads_ad_type_selection_product_accordian_item2)
            findViewById<Typography>(R.id.item3)?.text =
                getString(R.string.topads_ad_type_selection_product_accordian_item3)
            findViewById<ImageUnify>(R.id.image)?.urlSrc = IKLAN_PRODUCT_AD_TYPE_IMG_URL
            findViewById<UnifyButton>(R.id.submit).run {
                text = getString(R.string.topads_dashboard_create_product_advertisement)
                setOnClickListener {
                    val intent = RouteManager.getIntent(
                        this@TopAdsTypeSelectionActivity,
                        ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER
                    )
                    startActivityForResult(intent, AUTO_ADS_DISABLED)
                }
            }
        }

        findViewById<CardUnify>(R.id.topads_ad_card2).run {
            findViewById<Typography>(R.id.title)?.text =
                getString(R.string.topads_dashboard_ad_headline_type_selection_title)
            findViewById<Typography>(R.id.subtitle)?.text =
                getString(R.string.topads_ad_type_selection_shop_subtitle)
            val accordianData = findViewById<Group>(R.id.accordian_group)
            findViewById<Typography>(R.id.accordian_text)?.run {
                text = getString(R.string.topads_ad_type_selection_shop_accordian_title)
                setOnClickListener { expandAccordian(accordianData) }
            }
            findViewById<ImageUnify>(R.id.accordian_icon)?.setOnClickListener { expandAccordian(accordianData) }
            findViewById<Typography>(R.id.item1)?.text =
                getString(R.string.topads_ad_type_selection_shop_accordian_item1)
            findViewById<Typography>(R.id.item2)?.text =
                getString(R.string.topads_ad_type_selection_shop_accordian_item2)
            findViewById<Typography>(R.id.item3)?.text =
                getString(R.string.topads_ad_type_selection_shop_accordian_item3)
            findViewById<ImageUnify>(R.id.image)?.urlSrc = IKLAN_TOKO_AD_TYPE_IMG_URL
            findViewById<UnifyButton>(R.id.submit).run {
                text = getString(R.string.topads_dashboard_create_shop_advertisement)
                setOnClickListener {
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormClickEvent(
                        click_iklan_toko,
                        "{${userSession.shopId}}",
                        userSession.userId
                    )
                    RouteManager.route(
                        this@TopAdsTypeSelectionActivity,
                        ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION
                    )
                }
            }
        }
    }

    private fun expandAccordian(accordianData: Group){
        if (accordianData.isVisible)
            accordianData.gone()
        else
            accordianData.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTO_ADS_DISABLED) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
