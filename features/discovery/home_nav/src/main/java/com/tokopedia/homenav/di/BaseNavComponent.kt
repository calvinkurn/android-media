package com.tokopedia.homenav.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@BaseNavScope
@Component(modules = [BaseNavModule::class], dependencies = [BaseAppComponent::class])
interface BaseNavComponent {

}