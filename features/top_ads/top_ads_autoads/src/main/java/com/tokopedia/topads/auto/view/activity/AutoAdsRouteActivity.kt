package com.tokopedia.topads.auto.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.design.component.ToasterError

import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.base.AutoAdsBaseActivity
import com.tokopedia.topads.auto.router.TopAdsAutoRouter
import com.tokopedia.topads.auto.view.factory.TopAdsInfoViewModelFactory
import com.tokopedia.topads.auto.view.fragment.DailyBudgetFragment
import com.tokopedia.topads.auto.view.viewmodel.TopAdsInfoViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.topads.common.constant.TopAdsAddingOption
import com.tokopedia.topads.common.data.util.ApplinkUtil
import javax.inject.Inject

@DeepLink(ApplinkConst.SellerApp.TOPADS_AUTOADS)
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
            ToasterError.showClose(this, ErrorHandler.getErrorMessage(it, t))
            finish()
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
//        startActivity((application as TopAdsAutoRouter).getTopAdsDashboardIntent(this@AutoAdsRouteActivity))
        (application as TopAdsAutoRouter).openTopAdsDashboardApplink(this@AutoAdsRouteActivity)
    }

    private fun noProduct() {
        startActivity(Intent(this@AutoAdsRouteActivity, EmptyProductActivity::class.java))
    }

    private fun noAds() {
        startActivity(Intent(this@AutoAdsRouteActivity, StartAutoAdsActivity::class.java))
    }

    private fun manualAds() {
        (application as TopAdsAutoRouter).openTopAdsDashboardApplink(this@AutoAdsRouteActivity)
    }

    companion object {
        val TAG = AutoAdsRouteActivity::class.java.simpleName

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AutoAdsRouteActivity::class.java)
        }
    }

//    object DeepLinkIntents {
//        @DeepLink(ApplinkConst.SellerApp.TOPADS_AUTOADS)
//        @JvmStatic
//        fun getCallingApplinkIntent(context: Context, extras: Bundle): Intent {
//            if (GlobalConfig.isSellerApp()) {
//                val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
//                return getCallingIntent(context)
//                        .setData(uri.build())
//                        .putExtras(extras)
//            } else {
//                return ApplinkUtil.getSellerAppApplinkIntent(context, extras)
//            }
//        }
//    }
}
