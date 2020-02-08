package com.tokopedia.loginregister.shopcreation.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.DaggerShopCreationComponent
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent

/**
 * Created by Ade Fulki on 2019-12-19.
 * ade.hadian@tokopedia.com
 */

abstract class BaseShopCreationActivity : BaseSimpleActivity(), HasComponent<ShopCreationComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.WHITE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shop_creation
    }

    override fun getComponent(): ShopCreationComponent = DaggerShopCreationComponent
            .builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}