package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.buyer_request_cancel.di.BuyerRequestCancelRespondComponent
import com.tokopedia.sellerorder.buyer_request_cancel.di.DaggerBuyerRequestCancelRespondComponent
import timber.log.Timber

class BuyerRequestCancelRespondActivity : BaseSimpleActivity(), HasComponent<BuyerRequestCancelRespondComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDimming()
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

    private fun handleDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    override fun getComponent(): BuyerRequestCancelRespondComponent {
        return DaggerBuyerRequestCancelRespondComponent
            .builder()
            .somComponent(SomComponentInstance.getSomComponent(application))
            .build()
    }
}
