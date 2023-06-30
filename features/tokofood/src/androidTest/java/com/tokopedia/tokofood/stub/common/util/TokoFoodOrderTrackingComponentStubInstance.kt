package com.tokopedia.tokofood.stub.common.util

import android.app.Application
import android.content.Context
import com.tokochat.tokochat_config_common.util.TokoChatConnection
import com.tokopedia.tokofood.stub.postpurchase.di.component.DaggerTokoFoodOrderTrackingComponentStub
import com.tokopedia.tokofood.stub.postpurchase.di.component.TokoFoodOrderTrackingComponentStub
import com.tokopedia.tokofood.stub.postpurchase.di.module.TokoFoodCourierConversationModuleStub
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
                    .tokoChatConfigComponent(TokoChatConnection.getComponent(context))
                    .tokoFoodOrderTrackingModuleStub(TokoFoodOrderTrackingModuleStub())
                    .tokoFoodCourierConversationModuleStub(
                        TokoFoodCourierConversationModuleStub(context as Application)
                    )
                    .build().also { tokoFoodOrderTrackingComponentStub = it }
        }
    }
}
