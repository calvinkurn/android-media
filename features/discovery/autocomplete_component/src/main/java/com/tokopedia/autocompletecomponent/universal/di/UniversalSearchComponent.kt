package com.tokopedia.autocompletecomponent.universal.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.universal.UniversalSearchActivity
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModelFactoryModule
import dagger.Component

@UniversalSearchScope
@Component(modules = [
    UniversalSearchViewModelFactoryModule::class,
], dependencies = [BaseAppComponent::class])
interface UniversalSearchComponent {

    fun inject(activity: UniversalSearchActivity)
}