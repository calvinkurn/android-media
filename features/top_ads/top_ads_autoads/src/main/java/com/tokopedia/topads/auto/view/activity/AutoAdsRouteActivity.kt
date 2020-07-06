package com.tokopedia.topads.auto.view.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.AppUtil
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.base.AutoAdsBaseActivity
import com.tokopedia.topads.auto.view.factory.TopAdsInfoViewModelFactory
import com.tokopedia.topads.auto.view.viewmodel.TopAdsInfoViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AutoAdsRouteActivity : AutoAdsBaseActivity() {

    @Inject
    lateinit var factory: TopAdsInfoViewModelFactory
    @Inject
    lateinit var userSession: UserSessionInterface
    lateinit var adsInfoViewModel: TopAdsInfoViewModel


    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AutoAds_Transparent)
        setContentView(R.layout.activity_auto_ads_route)
        component.inject(this)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
        run {
            adsInfoViewModel = ViewModelProviders.of(this, factory).get(TopAdsInfoViewModel::class.java)
            adsInfoViewModel.getShopAdsInfo(userSession.shopId.toInt(), this::onFailShopInfo)
            adsInfoViewModel.shopInfoData.observe(this, Observer {
                when(it!!.category){
                    1 -> noProduct()
                    2 -> noAds()
                    3 -> manualAds()
                    4 -> autoAds()
                    else -> finish()
                }
                finish()
            })
        }
    }

    private fun onFailShopInfo(t: Throwable) {
        let {
            Toaster.make(findViewById(android.R.id.content), ErrorHandler.getErrorMessage(it, t),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(com.tokopedia.abstraction.R.string.close))
            finish()
        }
    }

    private fun autoAds() {
        openDashboard()
    }

    private fun noProduct() {
        startActivity(Intent(this@AutoAdsRouteActivity, EmptyProductActivity::class.java))
    }

    private fun noAds() {
        startActivity(RouteManager.getIntent(this@AutoAdsRouteActivity, ApplinkConstInternalTopAds.TOPADS_CREATE_ADS))

    }

    private fun manualAds() {
        openDashboard()
    }

    private fun openDashboard() {
        if (AppUtil.isSellerInstalled(this)) {
            RouteManager.route(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER)
        } else {
            RouteManager.route(this, ApplinkConstInternalMechant.MERCHANT_REDIRECT_CREATE_SHOP)
        }
    }

    companion object {
        val TAG = AutoAdsRouteActivity::class.java.simpleName

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AutoAdsRouteActivity::class.java)
        }
    }

}
