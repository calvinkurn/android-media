package com.tokopedia.mvcwidget.di.components

import com.tokopedia.mvcwidget.views.MvcDetailView
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.mvcwidget.di.module.DispatcherModule
import com.tokopedia.mvcwidget.di.module.ViewModelModule
import dagger.Component
import javax.inject.Scope

@Scope
@Retention
annotation class MvcScope

@MvcScope
@Component(modules = [DispatcherModule::class, ViewModelModule::class])
interface MvcComponent {
    fun inject(view: MvcView)
    fun inject(view: MvcDetailView)
}