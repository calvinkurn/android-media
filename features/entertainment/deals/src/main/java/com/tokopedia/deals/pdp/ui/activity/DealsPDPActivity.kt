package com.tokopedia.deals.pdp.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.DealsComponentInstance
import com.tokopedia.deals.pdp.di.DaggerDealsPDPComponent
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.fragment.DealsPDPFragment

class DealsPDPActivity: BaseSimpleActivity(), HasComponent<DealsPDPComponent> {

    var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            productId = uri.query ?: ""
        } else if (savedInstanceState != null) {
            productId = savedInstanceState.getString(EXTRA_PRODUCT_ID, "")
        }
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        return DealsPDPFragment.createInstance(productId)
    }

    override fun getComponent(): DealsPDPComponent {
        return DaggerDealsPDPComponent.builder()
            .dealsComponent(DealsComponentInstance.getDealsComponent(application, this))
            .build()
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
    }
}