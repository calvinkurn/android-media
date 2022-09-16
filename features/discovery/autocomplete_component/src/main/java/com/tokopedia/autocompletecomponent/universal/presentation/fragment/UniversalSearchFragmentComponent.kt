package com.tokopedia.autocompletecomponent.universal.presentation.fragment

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContextModule
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchIrisAnalyticModule
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchScope
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchSearchParameterModule
import com.tokopedia.autocompletecomponent.universal.di.UniversalTrackingQueueModule
import dagger.Component

@UniversalSearchScope
@Component(modules = [
    UniversalSearchContextModule::class,
    UniversalSearchIrisAnalyticModule::class,
    UniversalTrackingQueueModule::class,
    UniversalSearchSearchParameterModule::class,
], dependencies = [BaseAppComponent::class])
internal interface UniversalSearchFragmentComponent {

    fun inject(fragment: UniversalSearchFragment)
}