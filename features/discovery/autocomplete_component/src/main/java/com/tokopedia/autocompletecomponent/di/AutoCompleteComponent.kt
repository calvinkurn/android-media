package com.tokopedia.autocompletecomponent.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.BaseAutoCompleteActivity
import com.tokopedia.autocompletecomponent.unify.AutoCompleteStateModule
import com.tokopedia.autocompletecomponent.unify.byteio.SugSessionIdModule
import com.tokopedia.autocompletecomponent.unify.domain.usecase.AutoCompleteUseCaseModule
import com.tokopedia.autocompletecomponent.util.CoachMarkLocalCache
import com.tokopedia.autocompletecomponent.util.SchedulersProvider
import dagger.Component

@AutoCompleteScope
@Component(
    modules = [
        ViewModelModule::class,
        LocalCacheModule::class,
        SchedulersProviderModule::class,
        AutoCompleteStateModule::class,
        AutoCompleteFragmentModule::class,
        AutoCompleteUseCaseModule::class,
        AutoCompleteModule::class,
        IrisModule::class,
        SugSessionIdModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface AutoCompleteComponent {
    val viewModelFactory: ViewModelProvider.Factory
    val coachMarkLocalCache: CoachMarkLocalCache
    val schedulersProvider: SchedulersProvider

    fun inject(activity: BaseAutoCompleteActivity)
}
