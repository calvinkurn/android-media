package com.tokopedia.loginregister.shopcreation.view.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES.*
import android.view.View
import android.view.WindowManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.DaggerShopCreationComponent
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent

/**
 * Created by Ade Fulki on 2019-12-19.
 * ade.hadian@tokopedia.com
 */

abstract class BaseShopCreationActivity : BaseSimpleActivity(), HasComponent<ShopCreationComponent> {

    override fun getLayoutRes(): Int {
        return com.tokopedia.loginregister.R.layout.activity_shop_creation
    }

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in KITKAT until LOLLIPOP) {
            setWindowFlag(true)
        }

        if (Build.VERSION.SDK_INT >= KITKAT) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            setWindowFlag(false)
            window.statusBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun getComponent(): ShopCreationComponent = DaggerShopCreationComponent
            .builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(com.tokopedia.loginregister.R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }

    @TargetApi(KITKAT)
    private fun setWindowFlag(on: Boolean) {
        val winParams = window.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        window.attributes = winParams
    }
}
