package com.tokopedia.product.manage.stub.feature

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.product.manage.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.product.manage.stub.common.di.component.DaggerBaseAppComponentStub
import com.tokopedia.product.manage.stub.common.di.component.DaggerProductManageComponentStub
import com.tokopedia.product.manage.stub.common.di.component.ProductManageComponentStub

class ProductManageStubInstance {
    companion object {
        private var productManageComponent: ProductManageComponentStub? = null

        fun getComponent(context: Context): ProductManageComponentStub {
            val baseAppComponentStub =
                BaseAppComponentStubInstance.getBaseAppComponentStub(context.applicationContext)
            return productManageComponent?.run {
                productManageComponent
            } ?: DaggerProductManageComponentStub.builder()
                .baseAppComponentStub(baseAppComponentStub)
                .build()
        }
    }
}