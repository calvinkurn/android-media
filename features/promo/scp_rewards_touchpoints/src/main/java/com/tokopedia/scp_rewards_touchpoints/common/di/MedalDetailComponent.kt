package com.tokopedia.scp_rewards_touchpoints.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.scp_rewards_touchpoints.RewardsTouchPointActionsActivity
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.view.MedalCelebrationBottomSheet
import dagger.Component

@CelebrationScope
@Component(
    modules = [ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface CelebrationComponent {
    fun inject(testActivity: RewardsTouchPointActionsActivity)
    fun inject(celebrationBottomSheet: MedalCelebrationBottomSheet)
}
