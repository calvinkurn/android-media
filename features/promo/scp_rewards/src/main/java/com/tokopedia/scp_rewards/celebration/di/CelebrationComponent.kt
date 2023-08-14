package com.tokopedia.scp_rewards.celebration.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.scp_rewards.celebration.presentation.fragment.MedalCelebrationFragment
import dagger.Component

@CelebrationScope
@Component(
    modules = [ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface CelebrationComponent {
    fun inject(celebrationFragment: MedalCelebrationFragment)
}
