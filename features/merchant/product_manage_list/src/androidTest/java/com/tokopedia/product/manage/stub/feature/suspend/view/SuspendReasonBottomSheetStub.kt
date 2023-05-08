package com.tokopedia.product.manage.stub.feature.suspend.view

import android.os.Bundle
import com.tokopedia.product.manage.feature.suspend.view.bottomsheet.SuspendReasonBottomSheet
import com.tokopedia.product.manage.stub.feature.ProductManageStubInstance
import com.tokopedia.product.manage.stub.feature.suspend.di.DaggerSuspendReasonComponentStub
import com.tokopedia.product.manage.stub.feature.suspend.di.SuspendReasonComponentStub

class SuspendReasonBottomSheetStub : SuspendReasonBottomSheet() {

    companion object {
        fun createInstance(productId: String,
                           suspendListener: Listener
        ): SuspendReasonBottomSheet {
            return SuspendReasonBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_ID_KEY, productId)
                }
                listener = suspendListener
            }
        }


        private const val PRODUCT_ID_KEY = "product_id"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getComponent(): SuspendReasonComponentStub? {
        return activity?.run {
            DaggerSuspendReasonComponentStub.builder()
                .productManageComponentStub(ProductManageStubInstance.getComponent(applicationContext))
                .build()
        }
    }




}
