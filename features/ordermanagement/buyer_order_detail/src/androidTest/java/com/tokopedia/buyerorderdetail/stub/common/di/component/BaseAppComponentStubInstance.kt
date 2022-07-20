package com.tokopedia.buyerorderdetail.stub.common.di.component

import android.app.Application
import com.tokopedia.buyerorderdetail.stub.common.di.module.AppModuleStub

object BaseAppComponentStubInstance {
    private lateinit var baseAppComponentStub: BaseAppComponentStub

    fun getBaseAppComponentStub(
        application: Application
    ): BaseAppComponentStub {
        if (!::baseAppComponentStub.isInitialized) {
            baseAppComponentStub = DaggerBaseAppComponentStub.builder()
                .appModuleStub(AppModuleStub(application))
                .build()
        }

        return baseAppComponentStub
    }
}