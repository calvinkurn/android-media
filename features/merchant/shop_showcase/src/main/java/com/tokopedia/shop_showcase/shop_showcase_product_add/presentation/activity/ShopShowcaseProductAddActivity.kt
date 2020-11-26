package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.activity

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.ShopShowcaseEditParam
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment.ShopShowcaseProductAddFragment

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ShopShowcaseProductAddActivity: BaseSimpleActivity() {

    companion object {
        val ACTIVITY_LAYOUT = R.layout.activity_shop_showcase_product_add
        val PARENT_VIEW_ACTIVITY = R.id.parent_view
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

    override fun getParentViewResourceID(): Int {
        return PARENT_VIEW_ACTIVITY
    }

    override fun setupStatusBar() {
        val window: Window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().statusBarColor = ContextCompat.getColor(this, android.R.color.white)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}