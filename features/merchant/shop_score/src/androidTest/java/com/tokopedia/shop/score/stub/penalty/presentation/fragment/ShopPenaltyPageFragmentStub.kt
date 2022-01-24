package com.tokopedia.shop.score.stub.penalty.presentation.fragment

import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment
import com.tokopedia.shop.score.stub.common.util.ShopPenaltyComponentStubInstance

class ShopPenaltyPageFragmentStub: ShopPenaltyPageFragment() {

    override fun initInjector() {
        ShopPenaltyComponentStubInstance.getShopPenaltyComponentStub(
            context!!.applicationContext
        ).inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(): ShopPenaltyPageFragmentStub {
            return ShopPenaltyPageFragmentStub()
        }
    }
}