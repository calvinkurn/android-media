package com.tokopedia.scp_rewards.cabinet.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.scp_rewards.cabinet.presentation.ui.MedalCabinetFragment
import com.tokopedia.scp_rewards.cabinet.presentation.ui.SeeMoreMedaliFragment
import com.tokopedia.scp_rewards.detail.di.MedalDetailScope
import dagger.Component

@MedalDetailScope
@Component(
    modules = [MedalCabinetViewModelModule::class, MedalCabinetModule::class],
    dependencies = [BaseAppComponent::class]
)
interface MedalCabinetComponent {
    fun inject(medalCabinetFragment: MedalCabinetFragment)
    fun inject(seeMoreMedaliFragment: SeeMoreMedaliFragment)
}
