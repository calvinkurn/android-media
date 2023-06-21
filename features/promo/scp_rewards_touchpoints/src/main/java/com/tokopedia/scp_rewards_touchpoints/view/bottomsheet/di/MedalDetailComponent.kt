package com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.view.MedalCelebrationBottomSheet
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.view.MedalCelebrationFragment
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.viewmodel.MedalCelebrationViewModel
import dagger.Component

@CelebrationScope
@Component(
    modules = [ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface CelebrationComponent {
    fun inject(celebrationFragment: MedalCelebrationFragment)
    fun inject(celebrationBottomSheet: MedalCelebrationBottomSheet)
}
