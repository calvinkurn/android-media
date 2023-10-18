package com.tokopedia.tokofood.stub.common.util

import android.content.Context
import com.tokopedia.tokofood.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.tokofood.stub.common.di.component.DaggerBaseAppComponentStub
import com.tokopedia.tokofood.stub.common.di.module.AppModuleStub

class BaseAppComponentStubInstance {

    companion object {
        private var baseAppComponentStub: BaseAppComponentStub? = null

        fun getBaseAppComponentStub(
            context: Context
        ): BaseAppComponentStub {
            return baseAppComponentStub?.run { baseAppComponentStub }
                ?: DaggerBaseAppComponentStub.builder()
                    .appModuleStub(AppModuleStub(context))
                    .build().also { baseAppComponentStub = it }
        }
    }
}
