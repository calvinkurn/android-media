package com.tokopedia.shop.score.stub.common.util

import android.content.Context
import com.tokopedia.shop.score.stub.penalty.di.component.DaggerPenaltyComponentStub
import com.tokopedia.shop.score.stub.penalty.di.component.PenaltyComponentStub
import com.tokopedia.shop.score.stub.penalty.di.module.PenaltyModuleStub

class ShopPenaltyComponentStubInstance {

    companion object {
        private var penaltyComponentStub: PenaltyComponentStub? = null

        fun getShopPenaltyComponentStub(
            context: Context
        ): PenaltyComponentStub {
            val baseAppComponentStub =
                BaseAppComponentStubInstance.getBaseAppComponentStub(context)
            return penaltyComponentStub?.run { penaltyComponentStub }
                ?: DaggerPenaltyComponentStub
                    .builder()
                    .baseAppComponentStub(baseAppComponentStub)
                    .penaltyModuleStub(PenaltyModuleStub())
                    .build().also { penaltyComponentStub = it }
        }
    }
}