package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.ShopShowcaseEditParam
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.activity.ShopShowcaseAddActivity
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment.ShopShowcaseProductAddFragment

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShopShowcaseProductAddActivity: BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context, isActionEdit: Boolean): Intent {
            val intent = Intent(context, ShopShowcaseAddActivity::class.java)
            intent.putExtra(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT, isActionEdit)
            return intent
        }
        val ACTIVITY_LAYOUT = R.layout.activity_shop_showcase_product_add
    }

    private var isActionEdit: Boolean = false

    override fun getNewFragment(): Fragment? {
        intent?.extras?.let {
            isActionEdit = it.getBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT)
        }
        return ShopShowcaseProductAddFragment.createInstance(isActionEdit)
    }

    override fun getLayoutRes(): Int {
        return ACTIVITY_LAYOUT
    }

    override fun setupStatusBar() {
        val window: Window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().statusBarColor = ContextCompat.getColor(this, R.color.white)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}