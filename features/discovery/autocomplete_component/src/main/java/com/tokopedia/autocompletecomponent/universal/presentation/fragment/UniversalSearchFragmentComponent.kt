package com.tokopedia.autocompletecomponent.universal.presentation.fragment

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContextModule
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchIrisAnalyticModule
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchScope
import dagger.Component

@UniversalSearchScope
@Component(modules = [
    UniversalSearchContextModule::class,
    UniversalSearchIrisAnalyticModule::class,
], dependencies = [BaseAppComponent::class])
internal interface UniversalSearchFragmentComponent {

    fun inject(fragment: UniversalSearchFragment)
}