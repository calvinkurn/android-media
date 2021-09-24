package com.tokopedia.updateinactivephone.stub.di

import android.content.Context
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.stub.di.base.FakeBaseAppComponentBuilder
import com.tokopedia.updateinactivephone.stub.di.module.FakeInactivePhoneUseCaseModule

object InactivePhoneComponentStubBuilder {

    fun getComponent(applicationContext: Context, context: Context): InactivePhoneComponentStub {
        return DaggerInactivePhoneComponentStub
            .builder()
            .fakeBaseAppComponent(FakeBaseAppComponentBuilder.getComponent((applicationContext)))
            .inactivePhoneModule(InactivePhoneModule(context))
            .fakeInactivePhoneUseCaseModule(FakeInactivePhoneUseCaseModule())
            .build()
    }
}