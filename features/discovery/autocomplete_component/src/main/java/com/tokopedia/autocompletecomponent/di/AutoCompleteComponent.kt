package com.tokopedia.autocompletecomponent.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.autocompletecomponent.BaseAutoCompleteActivity
import com.tokopedia.autocompletecomponent.util.CoachMarkLocalCache
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
    val viewModelFactory: ViewModelProvider.Factory
    val coachMarkLocalCache: CoachMarkLocalCache

    fun inject(activity: BaseAutoCompleteActivity)
}
