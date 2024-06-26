package com.tokopedia.tokomember.di

import com.tokopedia.tokomember.TokomemberBottomSheetView
import dagger.Component
import javax.inject.Scope

@Scope
@Retention
annotation class TokomemberScope

@TokomemberScope
@Component(modules = [ TokomemberDispatcherModule::class])

interface TokomemberComponent {
    fun inject(view: TokomemberBottomSheetView)
}