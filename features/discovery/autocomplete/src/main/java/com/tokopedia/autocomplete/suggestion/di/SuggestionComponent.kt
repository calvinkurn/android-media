package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocomplete.suggestion.SuggestionFragment
import com.tokopedia.autocomplete.suggestion.SuggestionPresenter
import dagger.Component

@SuggestionScope
@Component(modules = [
    SuggestionUseCaseModule::class,
    SuggestionRepositoryModule::class,
    SuggestionMapperModule::class,
    SuggestionNetModule::class,
    SuggestionUserSessionInterfaceModule::class
], dependencies = [BaseAppComponent::class])
interface SuggestionComponent {
    fun inject(fragment: SuggestionFragment)

    fun inject(presenter: SuggestionPresenter)
}