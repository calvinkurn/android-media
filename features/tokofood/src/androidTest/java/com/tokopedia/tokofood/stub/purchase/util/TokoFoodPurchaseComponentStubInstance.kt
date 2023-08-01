package com.tokopedia.tokofood.stub.purchase.util

import android.content.Context
import com.tokopedia.tokofood.stub.common.util.BaseAppComponentStubInstance
import com.tokopedia.tokofood.stub.purchase.di.component.DaggerTokoFoodPurchaseComponentStub
import com.tokopedia.tokofood.stub.purchase.di.component.TokoFoodPurchaseComponentStub
import com.tokopedia.tokofood.stub.purchase.di.module.TokoFoodPurchaseModuleStub

class TokoFoodPurchaseComponentStubInstance {

    companion object {
        private var tokoFoodPurchaseComponentStub: TokoFoodPurchaseComponentStub? = null

        fun getTokoFoodPurchaseComponentStub(
            context: Context
        ): TokoFoodPurchaseComponentStub {
            val baseAppComponentStub =
                BaseAppComponentStubInstance.getBaseAppComponentStub(context)

            return tokoFoodPurchaseComponentStub
                ?: DaggerTokoFoodPurchaseComponentStub
                    .builder()
                    .baseAppComponentStub(baseAppComponentStub)
                    .tokoFoodPurchaseModuleStub(TokoFoodPurchaseModuleStub())
                    .build()
                    .also {
                        tokoFoodPurchaseComponentStub = it
                    }
        }
    }

}
