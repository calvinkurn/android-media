package com.tokopedia.tokofood.stub.purchase.promo.util

import android.content.Context
import com.tokopedia.tokofood.stub.common.util.BaseAppComponentStubInstance
import com.tokopedia.tokofood.stub.purchase.promo.di.DaggerTokoFoodPromoComponentStub
import com.tokopedia.tokofood.stub.purchase.promo.di.TokoFoodPromoComponentStub
import com.tokopedia.tokofood.stub.purchase.promo.di.TokoFoodPromoModuleStub

class TokoFoodPromoComponentStubInstance {

    companion object {
        private var tokoFoodPromoComponentStub: TokoFoodPromoComponentStub? = null

        fun getTokoFoodPromoComponentStub(
            context: Context
        ): TokoFoodPromoComponentStub {
            val baseAppComponentStub =
                BaseAppComponentStubInstance.getBaseAppComponentStub(context)

            return tokoFoodPromoComponentStub
                ?: DaggerTokoFoodPromoComponentStub
                    .builder()
                    .baseAppComponentStub(baseAppComponentStub)
                    .tokoFoodPromoModuleStub(TokoFoodPromoModuleStub())
                    .build()
                    .also {
                        tokoFoodPromoComponentStub = it
                    }
        }
    }

}
