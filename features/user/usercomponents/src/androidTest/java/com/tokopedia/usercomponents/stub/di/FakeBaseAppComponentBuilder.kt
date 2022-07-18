package com.tokopedia.usercomponents.stub.di

import android.content.Context

fun getComponent(applicationContext: Context): FakeBaseAppComponent =
    DaggerFakeBaseAppComponent.builder()
        .fakeAppModule(FakeAppModule(applicationContext))
        .build()