package com.tokopedia.scp_rewards.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.scp_rewards.detail.presentation.ui.MedalDetailFragment
import dagger.Component

@MedalDetailScope
@Component(
    modules = [MedalDetailViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface MedalDetailComponent {
    fun inject(medalDetailFragment: MedalDetailFragment)
}
