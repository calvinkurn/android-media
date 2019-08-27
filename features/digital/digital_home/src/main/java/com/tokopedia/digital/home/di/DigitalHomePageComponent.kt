package com.tokopedia.digital.home.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@DigitalHomePageScope
@Component(modules = [DigitalHomePageModule::class], dependencies = [BaseAppComponent::class])
interface DigitalHomePageComponent {

}