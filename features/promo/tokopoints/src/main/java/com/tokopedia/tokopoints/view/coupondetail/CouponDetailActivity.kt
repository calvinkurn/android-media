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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.BundleModule
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface


class CouponDetailActivity : BaseSimpleActivity(), HasComponent<TokopointBundleComponent> {
    val tokoPointComponent: TokopointBundleComponent by lazy { initInjector() }

    private var bundle: Bundle? = null
    private val REQUEST_CODE_LOGIN = 1

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

    override fun getNewFragment(): Fragment? {
        val userSession: UserSessionInterface = UserSession(this)
        return if (userSession.isLoggedIn) {
            CouponDetailFragment.newInstance(bundle ?: Bundle())
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            null
        }
    }

    override fun getComponent(): TokopointBundleComponent {
        return tokoPointComponent
    }

    private fun initInjector(): TokopointBundleComponent {
        return DaggerTokopointBundleComponent.builder()
                .bundleModule(BundleModule(bundle ?: Bundle()))
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .tokopointsQueryModule(TokopointsQueryModule(this))
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

    companion object {

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
