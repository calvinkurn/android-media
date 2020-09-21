package com.tokopedia.shop.settings.basicinfo.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.settings.basicinfo.oldview.activity.OldShopSettingsInfoActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remoteConfig = FirebaseRemoteConfigImpl(this)
        val isOldShopSettings = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_OLD_SHOP_SETTINGS, false)

        if(isOldShopSettings) {
            val intent = OldShopSettingsInfoActivity.createIntent(this)
            startActivity(intent)
            finish()
        }
    }


}
