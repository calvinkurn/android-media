package com.tokopedia.liveness.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.liveness.view.LivenessErrorFragment
import com.tokopedia.liveness.view.LivenessFragment
import dagger.Component

@LivenessDetectionScope
@Component(modules = [
    LivenessDetectionModule::class
], dependencies = [
    BaseAppComponent::class
])
interface LivenessDetectionComponent {
    fun inject(fragment: LivenessFragment)
    fun inject(fragment: LivenessErrorFragment)
}