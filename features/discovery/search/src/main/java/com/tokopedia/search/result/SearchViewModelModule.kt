package com.tokopedia.search.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_component.usecase.thematic.ThematicModel
import com.tokopedia.home_component.usecase.thematic.ThematicUsecaseUtil
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module(
    includes = [
        SearchStateModule::class
    ]
)
internal class SearchViewModelModule {

    @Provides
    @SearchScope
    fun provideViewModelProviders(
        viewModelProvider: ViewModelFactory
    ): ViewModelProvider.Factory = viewModelProvider

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    @SearchScope
    fun provideSearchViewModel(
        searchState: SearchState,
        coroutineDispatchers: CoroutineDispatchers,
        @Named(ThematicUsecaseUtil.THEMATIC_DI_NAME)
        thematicUseCase: UseCase<ThematicModel>,
    ): ViewModel =
        SearchViewModel(searchState, coroutineDispatchers, thematicUseCase)
}
