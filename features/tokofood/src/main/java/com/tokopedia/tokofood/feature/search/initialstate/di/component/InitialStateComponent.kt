package com.tokopedia.tokofood.feature.search.initialstate.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.feature.search.initialstate.di.module.InitialStateSearchModule
import com.tokopedia.tokofood.feature.search.initialstate.di.scope.InitialStateScope
import com.tokopedia.tokofood.feature.search.initialstate.presentation.fragment.InitialSearchStateFragment
import dagger.Component

@InitialStateScope
@Component(modules = [InitialStateSearchModule::class], dependencies = [BaseAppComponent::class])
internal interface InitialStateComponent {
    fun inject(fragment: InitialSearchStateFragment)
}