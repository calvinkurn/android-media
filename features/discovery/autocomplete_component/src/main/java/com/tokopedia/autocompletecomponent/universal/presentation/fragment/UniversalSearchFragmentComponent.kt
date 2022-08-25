package com.tokopedia.autocompletecomponent.universal.presentation.fragment

import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContextModule
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchScope
import dagger.Component

@UniversalSearchScope
@Component(modules = [
    UniversalSearchContextModule::class
])
internal interface UniversalSearchFragmentComponent {

    fun inject(fragment: UniversalSearchFragment)
}