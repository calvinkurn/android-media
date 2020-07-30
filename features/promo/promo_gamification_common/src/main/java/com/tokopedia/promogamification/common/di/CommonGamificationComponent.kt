package com.tokopedia.promogamification.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.promogamification.common.floating.view.fragment.FloatingEggButtonFragment
import dagger.Component

@CommonGamificationScope
@Component(modules = [CommonGamificationModule::class],dependencies = [BaseAppComponent::class])
interface CommonGamificationComponent {

    fun inject(fragment: FloatingEggButtonFragment)
}