package com.tokopedia.liveness.di

import com.tokopedia.liveness.view.fragment.LivenessErrorFragment
import com.tokopedia.liveness.view.fragment.LivenessFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.liveness.view.revamp.RevampLivenessErrorFragment
import com.tokopedia.liveness.view.revamp.RevampLivenessFragment
import dagger.Component

@LivenessDetectionScope
@Component(modules = [
    LivenessDetectionModule::class
], dependencies = [
    BaseAppComponent::class
])
interface LivenessDetectionComponent {
    fun inject(fragment: LivenessErrorFragment)
    fun inject(fragment: LivenessFragment)
    fun inject(fragment: RevampLivenessFragment)
    fun inject(fragment: RevampLivenessErrorFragment)
}