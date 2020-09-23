package com.tokopedia.seller.menu.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.base.BaseSellerMenuActivity
import com.tokopedia.seller.menu.presentation.fragment.SellerMenuFragment
import javax.inject.Inject

class SellerMenuActivity: BaseSellerMenuActivity() {

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
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