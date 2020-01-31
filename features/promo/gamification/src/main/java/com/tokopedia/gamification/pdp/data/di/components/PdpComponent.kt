package com.tokopedia.gamification.pdp.data.di.components

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gamification.pdp.data.di.modules.*
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.gamification.pdp.presentation.views.PdpGamificationView
import dagger.Component

@GamificationPdpScope
@Component(modules = [DispatcherModule::class,
    GqlQueryModule::class,
    PdpModule::class,
    ViewModelModule::class],dependencies = [BaseAppComponent::class])
interface PdpComponent {

    fun inject(view: PdpGamificationView)
}