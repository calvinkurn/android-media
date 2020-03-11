package com.tokopedia.attachvoucher.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachvoucher.R
import com.tokopedia.attachvoucher.di.AttachVoucherComponent
import com.tokopedia.attachvoucher.di.DaggerAttachVoucherComponent
import com.tokopedia.attachvoucher.view.fragment.AttachVoucherFragment

class AttachVoucherActivity : BaseSimpleActivity(), HasComponent<AttachVoucherComponent> {

    override fun getNewFragment(): Fragment? {
        val shopId = intent.getStringExtra(ApplinkConst.AttachVoucher.PARAM_SHOP_ID) ?: ""
        return AttachVoucherFragment.createInstance(shopId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useLightNotificationBar()
        setupToolbar()
    }

    override fun getComponent(): AttachVoucherComponent {
        return DaggerAttachVoucherComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    private fun setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
        supportActionBar?.setTitle(R.string.title_attachvoucher)
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }
}
