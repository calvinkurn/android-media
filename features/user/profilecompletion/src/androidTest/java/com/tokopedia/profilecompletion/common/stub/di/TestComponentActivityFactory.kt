package com.tokopedia.profilecompletion.common.stub.di

import android.content.Context

fun createProfileCompletionComponent(applicationContext: Context): TestProfileCompletionComponent =
    DaggerTestProfileCompletionComponent.builder()
        .testProfileCompletionSettingModule(TestProfileCompletionSettingModule(applicationContext))
        .build()
