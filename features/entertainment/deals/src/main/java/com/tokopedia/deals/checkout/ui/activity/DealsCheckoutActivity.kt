package com.tokopedia.deals.checkout.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.deals.DealsComponentInstance
import com.tokopedia.deals.checkout.di.DaggerDealsCheckoutComponent
import com.tokopedia.deals.checkout.di.DealsCheckoutComponent
import com.tokopedia.deals.checkout.ui.fragment.DealsCheckoutFragment
import com.tokopedia.deals.pdp.data.ProductDetailData

class DealsCheckoutActivity: BaseSimpleActivity(), HasComponent<DealsCheckoutComponent> {

    private var dealsDetail: ProductDetailData? = null
    private var dealsVerify: EventVerifyResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dealsDetail = intent.getParcelableExtra(EXTRA_DEAL_DETAIL)
        dealsVerify = intent.getParcelableExtra(EXTRA_DEAL_VERIFY)
    }

    override fun getNewFragment(): Fragment {
        return DealsCheckoutFragment.createInstance(dealsDetail, dealsVerify)
    }

    override fun getComponent(): DealsCheckoutComponent {
        return DaggerDealsCheckoutComponent.builder()
            .dealsComponent(DealsComponentInstance.getDealsComponent(application, this))
            .build()
    }

    companion object {
        const val EXTRA_DEAL_DETAIL = "EXTRA_DEAL_DETAIL"
        const val EXTRA_DEAL_VERIFY = "EXTRA_DEAL_VERIFY"
    }
}
