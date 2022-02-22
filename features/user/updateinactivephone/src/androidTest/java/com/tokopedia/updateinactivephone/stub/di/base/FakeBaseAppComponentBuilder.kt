package com.tokopedia.updateinactivephone.stub.di.base

import android.content.Context

object FakeBaseAppComponentBuilder {

    fun getComponent(applicationContext: Context): FakeBaseAppComponent {
        return DaggerFakeBaseAppComponent
            .builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
    }
}