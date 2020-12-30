package com.tokopedia.pms.howtopay_native.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Component

@HowToPayScope
@Component(modules = [ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface HowToPayComponent {
    @ApplicationContext
    fun context(): Context

}