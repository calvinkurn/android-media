package com.tokopedia.verification.stub.common.di

import android.content.Context

object FakeBaseAppComponentBuilder {

    fun getComponent(applicationContext: Context): FakeBaseAppComponent =
            DaggerFakeBaseAppComponent.builder()
                    .fakeAppModule(FakeAppModule(applicationContext))
                    .build()
}
