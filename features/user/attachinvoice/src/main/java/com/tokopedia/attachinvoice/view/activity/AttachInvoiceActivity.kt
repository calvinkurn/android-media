package com.tokopedia.attachinvoice.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.attachinvoice.di.AttachInvoiceComponent
import com.tokopedia.attachinvoice.di.DaggerAttachInvoiceComponent
import com.tokopedia.attachinvoice.view.fragment.AttachInvoiceFragment

class AttachInvoiceActivity : BaseSimpleActivity(), HasComponent<AttachInvoiceComponent> {

    override fun getNewFragment(): Fragment? {
        return AttachInvoiceFragment.createInstance(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useLightNotificationBar()
        setupToolbar()
    }

    override fun getComponent(): AttachInvoiceComponent {
        return DaggerAttachInvoiceComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    private fun setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }
}
