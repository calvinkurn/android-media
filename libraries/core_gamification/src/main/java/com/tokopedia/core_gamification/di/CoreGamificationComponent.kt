package com.tokopedia.core_gamification.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.core_gamification.floating.view.fragment.FloatingEggButtonFragment
import dagger.Component

@CoreGamificationScope
@Component(modules = [CoreGamificationModule::class],dependencies = [BaseAppComponent::class])
interface CoreGamificationComponent {

    fun inject(fragment: FloatingEggButtonFragment)
}