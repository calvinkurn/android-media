package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.di.AutoCompleteComponent
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTrackingTestModule
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStatePresenterModule
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateViewListenerModule
import com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch.DeleteRecentSearchUseCaseTestModule
import com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate.InitialStateUseCaseTestModule
import com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate.RefreshInitialStateUseCaseTestModule
import dagger.Component

@InitialStateScope
@Component(modules = [
    InitialStateUseCaseTestModule::class,
    RefreshInitialStateUseCaseTestModule::class,
    DeleteRecentSearchUseCaseTestModule::class,
    InitialStateTrackingTestModule::class,
    InitialStatePresenterModule::class,
    InitialStateViewListenerModule::class,
], dependencies = [BaseAppComponent::class])
interface InitialStateTestComponent: InitialStateComponent
