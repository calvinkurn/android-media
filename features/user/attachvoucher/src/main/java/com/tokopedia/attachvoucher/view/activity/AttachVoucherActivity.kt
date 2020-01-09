package com.tokopedia.attachvoucher.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.attachvoucher.di.AttachVoucherComponent
import com.tokopedia.attachvoucher.di.DaggerAttachVoucherComponent
import com.tokopedia.attachvoucher.view.fragment.AttachVoucherFragment

class AttachVoucherActivity : BaseSimpleActivity(), HasComponent<AttachVoucherComponent>,
        AttachVoucherFragment.Listener {

    override fun getNewFragment(): Fragment? {
        return AttachVoucherFragment.createInstance(intent.extras)
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
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    override fun onClickAttachVoucher(intent: Intent) {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
