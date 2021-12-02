package com.tokopedia.shop.score.uitest.stub.common.util

import android.app.Application
import com.tokopedia.shop.score.uitest.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.shop.score.uitest.stub.common.di.component.DaggerBaseAppComponentStub
import com.tokopedia.shop.score.uitest.stub.common.di.module.AppModuleStub

class BaseAppComponentStubInstance {

    companion object {
        private var baseAppComponentStub: BaseAppComponentStub? = null

        fun getBaseAppComponentStub(
            application: Application
        ): BaseAppComponentStub {
            return baseAppComponentStub?.run { baseAppComponentStub }
                ?: DaggerBaseAppComponentStub.builder()
                    .appModuleStub(AppModuleStub(application))
                    .build()
        }
    }
}