package com.tokopedia.shop.score.stub.penalty.presentation.fragment

import com.tokopedia.shop.score.penalty.presentation.old.fragment.ShopPenaltyPageOldFragment
import com.tokopedia.shop.score.stub.common.util.ShopPenaltyComponentStubInstance

class ShopPenaltyPageFragmentStub: ShopPenaltyPageOldFragment() {

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
