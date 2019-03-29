package com.tokopedia.topads.dashboard.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.topads.common.data.util.ApplinkUtil
import com.tokopedia.topads.dashboard.TopAdsDashboardRouter
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAddCreditFragment

/**
 * Created by Nathaniel on 11/22/2016.
 */

class TopAdsAddCreditActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {

    override fun getNewFragment() = TopAdsAddCreditFragment.createInstance()

    override fun getScreenName(): String? = null

    override fun onBackPressed() {
        if ((intent.extras?.getBoolean(TopAdsDashboardConstant.EXTRA_APPLINK_FROM_PUSH, false) == true)
                && application is TopAdsDashboardRouter) {
            val homeIntent: Intent = (application as TopAdsDashboardRouter).getHomeIntent(this)
            startActivity(homeIntent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    companion object {

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, TopAdsAddCreditActivity::class.java)
        }
    }

    object DeepLinkIntents {
        @DeepLink(ApplinkConst.SellerApp.TOPADS_CREDIT)
        @JvmStatic
        fun getCallingApplinkIntent(context: Context, extras: Bundle): Intent? {
            if (GlobalConfig.isSellerApp()) {
                val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
                return getCallingIntent(context)
                        .setData(uri.build())
                        .putExtras(extras)
            } else {
                return ApplinkUtil.getSellerAppApplinkIntent(context, extras)
            }
        }
    }
}