package com.tokopedia.autocompletecomponent.initialstate.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.di.AutoCompleteComponent
import com.tokopedia.autocompletecomponent.initialstate.InitialStateFragment
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTrackingModule
import com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch.DeleteRecentSearchUseCaseModule
import com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate.InitialStateUseCaseModule
import com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate.RefreshInitialStateUseCaseModule
import dagger.Component

@InitialStateScope
@Component(modules = [
    InitialStateUseCaseModule::class,
    RefreshInitialStateUseCaseModule::class,
    DeleteRecentSearchUseCaseModule::class,
    InitialStateTrackingModule::class,
    InitialStatePresenterModule::class,
    InitialStateViewListenerModule::class,
], dependencies = [BaseAppComponent::class])
interface InitialStateComponent {

    fun inject(fragment: InitialStateFragment)
}
