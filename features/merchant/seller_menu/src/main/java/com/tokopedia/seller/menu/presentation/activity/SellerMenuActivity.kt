package com.tokopedia.seller.menu.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.gm.common.utils.PMShopScoreInterruptHelper
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.base.BaseSellerMenuActivity
import com.tokopedia.seller.menu.presentation.fragment.SellerMenuFragment
import javax.inject.Inject

class SellerMenuActivity : BaseSellerMenuActivity() {

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    @Inject
    lateinit var pmShopScoreInterruptHelper: PMShopScoreInterruptHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        showInterruptToaster(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        showInterruptToaster(intent)
    }

    private fun showInterruptToaster(intent: Intent?) {
        intent?.data?.let { uri ->
            val parentView = findViewById<FrameLayout>(R.id.parent_view)
            pmShopScoreInterruptHelper.setShopScoreConsentStatus(uri) {
                if (it) {
                    pmShopScoreInterruptHelper.showsShopScoreConsentToaster(parentView)
                }
            }

            pmShopScoreInterruptHelper.showToasterPmProInterruptPage(uri, parentView)
        }
    }

    override fun getNewFragment(): Fragment? {
        return SellerMenuFragment()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        sellerMenuTracker.sendEventCloseShopAccount()
    }

    private fun initInjector() {
        DaggerSellerMenuComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }
}