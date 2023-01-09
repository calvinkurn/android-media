package com.tokopedia.buyerorderdetail.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent

class PartialOrderFulfillmentFragment: BaseDaggerFragment() {

    override fun getScreenName(): String = PartialOrderFulfillmentFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(BuyerOrderDetailComponent::class.java).inject(this)
    }

    companion object {
        fun newInstance(): PartialOrderFulfillmentFragment {
            return PartialOrderFulfillmentFragment()
        }
    }
}
