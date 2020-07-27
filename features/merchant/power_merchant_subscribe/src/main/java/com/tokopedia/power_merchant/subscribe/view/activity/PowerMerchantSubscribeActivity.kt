package com.tokopedia.power_merchant.subscribe.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.gm.common.constant.GMParamTracker
import com.tokopedia.gm.common.utils.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment
import javax.inject.Inject

/**
 * Deeplink POWER_MERCHANT_SUBSCRIBE
 */
class PowerMerchantSubscribeActivity : BaseSimpleActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, PowerMerchantSubscribeActivity::class.java)
        }
    }

    @Inject
    lateinit var powerMerchantTracking: PowerMerchantTracking

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        return PowerMerchantSubscribeFragment.createInstance()
    }

    override fun getScreenName(): String = GMParamTracker.ScreenName.PM_UPGRADE_SHOP

    override fun sendScreenAnalytics() {
        powerMerchantTracking.eventOpenScreen(screenName)
    }

    private fun initInjector() {
        val appComponent = (application as BaseMainApplication).baseAppComponent
        DaggerPowerMerchantSubscribeComponent.builder()
            .baseAppComponent(appComponent)
            .build()
            .inject(this)
    }
}
