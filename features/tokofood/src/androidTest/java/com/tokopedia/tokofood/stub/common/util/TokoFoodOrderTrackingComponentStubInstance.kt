package com.tokopedia.tokofood.stub.common.util

import android.content.Context
import com.tokopedia.tokofood.stub.postpurchase.di.component.DaggerTokoFoodOrderTrackingComponentStub
import com.tokopedia.tokofood.stub.postpurchase.di.component.TokoFoodOrderTrackingComponentStub
import com.tokopedia.tokofood.stub.postpurchase.di.module.TokoFoodOrderTrackingModuleStub

class TokoFoodOrderTrackingComponentStubInstance {

    companion object {
        private var tokoFoodOrderTrackingComponentStub: TokoFoodOrderTrackingComponentStub? = null

        fun getTokoFoodOrderTrackingComponentStub(
            context: Context
        ): TokoFoodOrderTrackingComponentStub {
            val baseAppComponentStub =
                BaseAppComponentStubInstance.getBaseAppComponentStub(context)
            return tokoFoodOrderTrackingComponentStub?.run { tokoFoodOrderTrackingComponentStub }
                ?: DaggerTokoFoodOrderTrackingComponentStub
                    .builder()
                    .baseAppComponentStub(baseAppComponentStub)
                    .tokoFoodOrderTrackingModuleStub(TokoFoodOrderTrackingModuleStub())
                    .build().also { tokoFoodOrderTrackingComponentStub = it }
        }
    }
}
