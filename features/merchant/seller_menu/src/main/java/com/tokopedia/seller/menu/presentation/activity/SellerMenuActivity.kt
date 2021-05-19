package com.tokopedia.seller.menu.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.base.BaseSellerMenuActivity
import com.tokopedia.seller.menu.presentation.fragment.SellerMenuFragment
import javax.inject.Inject

class SellerMenuActivity : BaseSellerMenuActivity() {

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val sellerMenuLifecycleState = (fragment as? SellerMenuFragment)?.lifecycle?.currentState
        if (sellerMenuLifecycleState?.isAtLeast(Lifecycle.State.CREATED) == true) {
            (fragment as? SellerMenuFragment)?.onNewIntent(intent?.data)
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