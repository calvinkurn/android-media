package com.tokopedia.creation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@CreationScope
@Component(modules = [CreationModule::class], dependencies = [BaseAppComponent::class])
interface CreationComponent {
}