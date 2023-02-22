package com.tokopedia.search.result.mps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class MPSViewModelModule {

    @Provides
    @SearchScope
    fun provideMPSViewModelProviders(
        viewModelProvider: ViewModelFactory
    ): ViewModelProvider.Factory = viewModelProvider

    @Provides
    @IntoMap
    @ViewModelKey(MPSViewModel::class)
    @SearchScope
    fun provideMPSViewModel(mpsViewModel: MPSViewModel): ViewModel = mpsViewModel
}
