package com.tokopedia.shop.settings.basicinfo.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    private fun setup() {
        window.decorView.setBackgroundColor(Color.WHITE)
        toolbar.background = ContextCompat.getDrawable(this, android.R.color.transparent)
    }

}
