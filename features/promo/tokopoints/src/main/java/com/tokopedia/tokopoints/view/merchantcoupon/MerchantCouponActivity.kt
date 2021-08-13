package com.tokopedia.tokopoints.view.merchantcoupon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule

class MerchantCouponActivity : BaseSimpleActivity() , HasComponent<TokopointBundleComponent> {
    private val tokoPointComponent: TokopointBundleComponent by lazy { initInjector() }
    private var bundle: Bundle? = null


    override fun getNewFragment(): Fragment? {
        return MerchantCouponFragment()
    }

    override fun getComponent(): TokopointBundleComponent {
        return tokoPointComponent
    }

    private fun initInjector() = DaggerTokopointBundleComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .tokopointsQueryModule(TokopointsQueryModule(this))
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        checkForDeeplink()
        super.onCreate(savedInstanceState)
        toolbar.hide()
        updateTitle(getString(R.string.tp_kupon_toko))
    }

    private fun checkForDeeplink() {
        bundle = intent.extras
        if (bundle == null) bundle = Bundle()
    }

    companion object {
        fun getCallingIntent(context: Context?, extras: Bundle?): Intent? {
            val intent = Intent(context, MerchantCouponActivity::class.java)
            intent.putExtras(extras ?: Bundle())
            return intent
        }

        fun getMerchantCouponList(context: Context?, extras: Bundle?): Intent? {
            return getCallingIntent(context, extras)
        }
    }
}