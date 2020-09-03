package com.tokopedia.seller.menu.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.seller.menu.presentation.base.BaseSellerMenuActivity
import com.tokopedia.seller.menu.presentation.fragment.SellerSettingsFragment

class SellerSettingsActivity: BaseSellerMenuActivity() {

    override fun getNewFragment(): Fragment? {
        return SellerSettingsFragment()
    }
}