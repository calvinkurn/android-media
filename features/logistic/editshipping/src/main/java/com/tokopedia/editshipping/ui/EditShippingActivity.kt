package com.tokopedia.editshipping.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.util.SCREEN_CONFIG_S_SHIPPING

class EditShippingActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.main_view, EditShippingFragment.newInstance()).commit()
        }

    }

    @Nullable
    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_shipping_shop_editor
    }

    override fun getScreenName(): String {
        return SCREEN_CONFIG_S_SHIPPING
    }

    override fun getToolbarResourceID(): Int {
        return R.id.shipping_shop_editor_toolbar
    }

}