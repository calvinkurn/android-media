package com.tokopedia.product.manage.stub.common.di.component

import android.content.Context
import com.tokopedia.product.manage.stub.common.di.module.AppModuleStub

object BaseAppComponentStubInstance {
    private lateinit var baseAppComponentStub: BaseAppComponentStub

    fun getBaseAppComponentStub(
        context: Context
    ): BaseAppComponentStub {
        if (!::baseAppComponentStub.isInitialized) {
            baseAppComponentStub = DaggerBaseAppComponentStub.builder()
                .appModuleStub(AppModuleStub(context))
                .build()
        }

        return baseAppComponentStub
    }
}