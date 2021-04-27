package com.tokopedia.power_merchant.subscribe.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.data.source.local.model.PowerMerchantSettingInfoUiModel
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscriptionFragment
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantSubscribeFragment
import kotlinx.android.synthetic.main.activity_pm_subsription.*

/**
 * Created By @ilhamsuaib on 25/02/21
 */

class SubscriptionActivity : BaseActivity(), SubscriptionActivityInterface, HasComponent<PowerMerchantSubscribeComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        openPowerMerchantWebView()
        setContentView(R.layout.activity_pm_subsription)
        window.decorView.setBackgroundColor(getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    override fun getComponent(): PowerMerchantSubscribeComponent {
        val appComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerPowerMerchantSubscribeComponent.builder()
                .baseAppComponent(appComponent)
                .build()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun switchToPmRevampPage(pmSettingInfo: PowerMerchantSettingInfoUiModel) {
        val fragment = PowerMerchantSubscriptionFragment.createInstance().apply {
            setPmSettingInfo(pmSettingInfo)
        }
        showFragment(fragment)
    }

    private fun openPowerMerchantWebView() {
        val applink = String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, PMConstant.Urls.POWER_MERCHANT_PAGE)
        RouteManager.route(this, applink)
        finish()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setupView() {
        setSupportActionBar(toolbarPmSubscription)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setWhiteStatusBar()
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    private fun showFragment(fragment: Fragment) {
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.framePmFragmentContainer, fragment)
                    .commit()
        }
    }
}