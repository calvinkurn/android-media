package com.tokopedia.autocompletecomponent.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.BaseAutoCompleteActivity
import dagger.Component

@AutoCompleteScope
@Component(
    modules = [
        ViewModelModule::class,
        LocalCacheModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface AutoCompleteComponent {
    val viewModelFactory : ViewModelProvider.Factory

    fun inject(activity: BaseAutoCompleteActivity)
}
