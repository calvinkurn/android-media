package com.tokopedia.seller.menu.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.base.BaseSellerMenuActivity
import com.tokopedia.seller.menu.presentation.fragment.SellerSettingsFragment
import javax.inject.Inject

class SellerSettingsActivity: BaseSellerMenuActivity() {

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        return SellerSettingsFragment()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        sellerMenuTracker.sendEventClickBackArrow()
    }

    private fun initInjector() {
        DaggerSellerMenuComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}