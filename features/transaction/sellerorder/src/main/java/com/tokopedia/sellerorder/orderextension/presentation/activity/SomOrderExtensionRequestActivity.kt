package com.tokopedia.sellerorder.orderextension.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.di.DaggerSomOrderExtensionRequestComponent
import com.tokopedia.sellerorder.orderextension.di.SomOrderExtensionRequestComponent
import com.tokopedia.sellerorder.orderextension.presentation.fragment.SomOrderExtensionRequestFragment
import timber.log.Timber

class SomOrderExtensionRequestActivity : BaseSimpleActivity(), HasComponent<SomOrderExtensionRequestComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()
        adjustDimming()
    }

    override fun getComponent(): SomOrderExtensionRequestComponent {
        return DaggerSomOrderExtensionRequestComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_buyer_request_cancel_respond
    }

    override fun getParentViewResourceID(): Int {
        return R.id.fragment_host
    }

    override fun getNewFragment(): Fragment? {
        return SomOrderExtensionRequestFragment()
    }

    private fun adjustDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
