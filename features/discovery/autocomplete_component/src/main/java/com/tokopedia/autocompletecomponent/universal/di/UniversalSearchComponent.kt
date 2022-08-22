package com.tokopedia.autocompletecomponent.universal.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.universal.domain.getuniversalsearch.UniversalSearchUseCaseModule
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment
import com.tokopedia.autocompletecomponent.universal.presentation.mapper.UniversalSearchModelMapperModule
import dagger.Component

@UniversalSearchScope
@Component(modules = [
    UniversalSearchUseCaseModule::class,
    UniversalSearchModelMapperModule::class,
], dependencies = [BaseAppComponent::class])
interface UniversalSearchComponent {

    fun inject(fragment: UniversalSearchFragment)
}