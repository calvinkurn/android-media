package com.tokopedia.tokopoints.view.catalogdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant

class CouponCatalogDetailsActivity : BaseSimpleActivity(), HasComponent<TokopointBundleComponent> {
    private val tokoPointComponent: TokopointBundleComponent by lazy { initInjector() }
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        forDeeplink()
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.tp_title_detail))
    }

    private fun forDeeplink() {
        bundle = intent.extras
        if (bundle == null) bundle = Bundle()
        if (intent.data != null) {
            UriUtil.destructiveUriBundle(ApplinkConstInternalPromo.TOKOPOINTS_CATALOG_DETAIL, intent.data, bundle)
        }
    }

    override fun getNewFragment(): Fragment? {
        return CouponCatalogFragment.newInstance(bundle)
    }

    override fun getComponent(): TokopointBundleComponent {
        return tokoPointComponent
    }

    private fun initInjector() = DaggerTokopointBundleComponent.builder()
        .baseAppComponent((application as BaseMainApplication).baseAppComponent)
        .tokopointsQueryModule(TokopointsQueryModule(this))
        .build()

    override fun onBackPressed() {
        super.onBackPressed()
        if (intent.getStringExtra(CommonConstant.EXTRA_CATALOG_CODE) == null) {
            AnalyticsTrackerUtil.sendEvent(applicationContext,
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.CLICK_BACK_ARROW,
                AnalyticsTrackerUtil.EventKeys.BACK_ARROW_LABEL)
        } else {
            AnalyticsTrackerUtil.sendEvent(applicationContext,
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.CLICK_BACK_ARROW,
                AnalyticsTrackerUtil.EventKeys.BACK_ARROW_LABEL)
        }
    }

    companion object {
        fun getCallingIntent(context: Context?, extras: Bundle?): Intent? {
            val intent = Intent(context, CouponCatalogDetailsActivity::class.java)
            intent.putExtras(extras)
            return intent
        }

        fun getCatalogDetail(context: Context?, extras: Bundle?): Intent? {
            return getCallingIntent(context, extras)
        }
    }

}