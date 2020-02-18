package com.tokopedia.autocomplete.initialstate

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocomplete.di.AutoCompleteScope
import com.tokopedia.autocomplete.di.UserSessionInterfaceModule
import com.tokopedia.autocomplete.initialstate.di.*
import dagger.Component

@AutoCompleteScope
@Component(modules = [
    InitialStateUseCaseModule::class,
    PopularSearchUseCaseModule::class,
    DeleteRecentSearchUseCaseModule::class,
    InitialStateRepositoryModule::class,
    InitialStateMapperModule::class,
    PopularSearchMapperModule::class,
    InitialStateNetModule::class,
    UserSessionInterfaceModule::class
], dependencies = [BaseAppComponent::class])
interface InitialStateComponent {
    fun inject(fragment: InitialStateFragment)

    fun inject(initialStatePresenter: InitialStatePresenter)
}