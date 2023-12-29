package com.tokopedia.tokofood.stub.purchase.promo.presentation.fragment

import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoFragment
import com.tokopedia.tokofood.stub.purchase.promo.util.TokoFoodPromoComponentStubInstance

class TokoFoodPromoFragmentStub: TokoFoodPromoFragment() {

    override fun initInjector() {
        context?.applicationContext?.let {
            TokoFoodPromoComponentStubInstance.getTokoFoodPromoComponentStub(it).inject(this)
        }
    }

    companion object {
        @JvmStatic
        fun createInstance(): TokoFoodPromoFragmentStub {
            return TokoFoodPromoFragmentStub()
        }
    }

}
