package com.tokopedia.gamification.pdp.data.di.components

import com.tokopedia.gamification.pdp.data.di.modules.*
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.gamification.pdp.presentation.views.PdpGamificationView
import dagger.Component

@GamificationPdpScope
@Component(modules = [DispatcherModule::class, GqlQueryModule::class, PdpModule::class, ViewModelModule::class, AppModule::class])
interface PdpComponent {

    fun inject(view: PdpGamificationView)
}