package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocomplete.initialstate.InitialStateFragment
import com.tokopedia.autocomplete.initialstate.InitialStatePresenter
import dagger.Component

@InitialStateScope
@Component(modules = [
    InitialStateUseCaseModule::class,
    RefreshInitialStateUseCaseModule::class,
    DeleteRecentSearchUseCaseModule::class,
    InitialStateRepositoryModule::class,
    InitialStateMapperModule::class,
    InitialStateNetModule::class,
    InitialStateUserSessionInterfaceModule::class,
    InitialStateContextModule::class,
    InitialStateIrisAnalyticsModule::class
], dependencies = [BaseAppComponent::class])
interface InitialStateComponent {
    fun inject(fragment: InitialStateFragment)

    fun inject(initialStatePresenter: InitialStatePresenter)
}