package com.tokopedia.topads.auto.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.utils.GlobalConfig

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.base.AutoAdsBaseActivity
import com.tokopedia.topads.auto.internal.AutoAdsLinkConstant
import com.tokopedia.topads.auto.router.TopAdsAutoRouter
import com.tokopedia.topads.auto.view.factory.TopAdsInfoViewModelFactory
import com.tokopedia.topads.auto.view.fragment.DailyBudgetFragment
import com.tokopedia.topads.auto.view.viewmodel.TopAdsInfoViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.topads.common.constant.TopAdsAddingOption
import javax.inject.Inject

@DeepLink(AutoAdsLinkConstant.AUTOADS_ROUTE_LINK)
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
        setContentView(R.layout.activity_auto_ads_route)
        component.inject(this)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
        run {
            adsInfoViewModel = ViewModelProviders.of(this, factory).get(TopAdsInfoViewModel::class.java)
            adsInfoViewModel.getShopAdsInfo(userSession.shopId.toInt())
            adsInfoViewModel.shopInfoData.observe(this, Observer {
                when(it!!.category){
                    1 -> noProduct()
                    2 -> noAds()
                    3 -> manualAds()
                    4 -> autoAds()
                }
                finish()
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DailyBudgetFragment.REQUEST_CODE_AD_OPTION) {
            if (data != null) {
                when (data.getIntExtra(DailyBudgetFragment.SELECTED_OPTION, -1)) {
                    TopAdsAddingOption.GROUP_OPT -> onSummaryGroupClicked()
                    TopAdsAddingOption.PRODUCT_OPT -> gotoCreateProductAd()
                    TopAdsAddingOption.KEYWORDS_OPT -> gotoCreateKeyword()
                }
                finish()
            }
        }
    }

    private fun autoAds() {
        startActivity((application as TopAdsAutoRouter).getTopAdsDashboardIntent(this@AutoAdsRouteActivity))
    }

    private fun noProduct() {
        startActivity(Intent(this@AutoAdsRouteActivity, EmptyProductActivity::class.java))
    }

    private fun noAds() {
        startActivity(Intent(this@AutoAdsRouteActivity, StartAutoAdsActivity::class.java))
    }

    private fun manualAds() {
        startActivity((application as TopAdsAutoRouter).getTopAdsDashboardIntent(this@AutoAdsRouteActivity))
    }

}
