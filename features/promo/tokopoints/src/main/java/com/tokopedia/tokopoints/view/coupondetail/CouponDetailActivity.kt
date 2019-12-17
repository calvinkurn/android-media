package com.tokopedia.tokopoints.view.coupondetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle

import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent
import com.tokopedia.tokopoints.di.TokoPointComponent
import com.tokopedia.tokopoints.view.fragment.CouponDetailFragment
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil

class CouponDetailActivity : BaseSimpleActivity(), HasComponent<TokoPointComponent> {
    private var tokoPointComponent: TokoPointComponent? = null
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        forDeeplink()
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.tp_title_detail))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
        setResult(Activity.RESULT_OK, intent)
    }

    private fun forDeeplink() {
        bundle = intent.extras
        if (intent.data != null) {
            bundle = UriUtil.destructiveUriBundle(ApplinkConstInternalPromo.TOKOPOINTS_COUPON_DETAIL, intent.data, bundle)

        }
    }

    override fun getNewFragment(): Fragment {
        return CouponDetailFragment.newInstance(bundle)
    }

    override fun getComponent(): TokoPointComponent? {
        if (tokoPointComponent == null) initInjector()
        return tokoPointComponent
    }

    private fun initInjector() {
        tokoPointComponent = DaggerTokoPointComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        AnalyticsTrackerUtil.sendEvent(applicationContext,
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.CLICK_BACK_ARROW,
                AnalyticsTrackerUtil.EventKeys.BACK_ARROW_LABEL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            inflateFragment()
        } else {
            finish()
        }
    }

    companion object {
        private val REQUEST_CODE_LOGIN = 1

        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, CouponDetailActivity::class.java)
            intent.putExtras(extras)
            return intent
        }

        fun getCouponDetail(context: Context, extras: Bundle): Intent {
            return getCallingIntent(context, extras)
        }
    }
}
