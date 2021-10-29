package com.tokopedia.home_account.stub.di

import android.content.Context

object FakeBaseAppComponentBuilder {

    fun getComponent(applicationContext: Context): FakeBaseAppComponent =
        DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
}