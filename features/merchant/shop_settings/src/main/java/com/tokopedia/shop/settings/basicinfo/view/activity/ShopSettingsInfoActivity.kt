package com.tokopedia.shop.settings.basicinfo.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopSettingsInfoFragment

/**
 * Created by Zulfikar on 5/19/2016.
 * deeplink: SHOP_SETTING_INFO
 */
class ShopSettingsInfoActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopSettingsInfoActivity::class.java)
    }

    override fun getNewFragment(): Fragment {
        return ShopSettingsInfoFragment.newInstance()
    }

}
