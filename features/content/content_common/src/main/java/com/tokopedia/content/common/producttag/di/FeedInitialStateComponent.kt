package com.tokopedia.content.common.producttag.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStatePresenterModule
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateViewListenerModule
import com.tokopedia.autocompletecomponent.initialstate.domain.deleterecentsearch.DeleteRecentSearchUseCaseModule
import com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate.InitialStateUseCaseModule
import com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate.RefreshInitialStateUseCaseModule
import com.tokopedia.content.common.producttag.di.module.FeedInitialStateTrackingModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
@InitialStateScope
@Component(
    modules = [
        InitialStateUseCaseModule::class,
        RefreshInitialStateUseCaseModule::class,
        DeleteRecentSearchUseCaseModule::class,
        FeedInitialStateTrackingModule::class,
        InitialStatePresenterModule::class,
        InitialStateViewListenerModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface FeedInitialStateComponent : InitialStateComponent