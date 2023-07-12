package com.tokopedia.scp_rewards_touchpoints.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.scp_rewards_touchpoints.TestActivity
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.view.MedalCelebrationBottomSheet
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.view.MedalCelebrationFragment
import dagger.Component

@CelebrationScope
@Component(
    modules = [ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface CelebrationComponent {
    fun inject(celebrationFragment: MedalCelebrationFragment)
    fun inject(testActivity: TestActivity)
    fun inject(celebrationBottomSheet: MedalCelebrationBottomSheet)
}
