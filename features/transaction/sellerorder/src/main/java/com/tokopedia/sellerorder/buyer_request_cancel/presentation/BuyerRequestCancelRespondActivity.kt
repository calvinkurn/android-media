package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.buyer_request_cancel.di.BuyerRequestCancelRespondComponent
import com.tokopedia.sellerorder.buyer_request_cancel.di.DaggerBuyerRequestCancelRespondComponent
import timber.log.Timber

class BuyerRequestCancelRespondActivity : BaseSimpleActivity(), HasComponent<BuyerRequestCancelRespondComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()
        adjustDimming()
    }

    override fun getComponent(): BuyerRequestCancelRespondComponent {
        return DaggerBuyerRequestCancelRespondComponent
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
        return BuyerRequestCancelRespondFragment()
    }

    private fun adjustDimming() {
        runCatching {
            window.setDimAmount(0f)
        }.onFailure {
            Timber.e(it)
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
