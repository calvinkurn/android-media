package com.tokopedia.shop.score.uitest.stub.common.util

import android.app.Application
import com.tokopedia.shop.score.uitest.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.shop.score.uitest.stub.common.di.module.AppModuleStub

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