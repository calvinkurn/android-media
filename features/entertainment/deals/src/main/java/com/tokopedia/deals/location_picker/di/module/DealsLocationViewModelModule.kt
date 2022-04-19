package com.tokopedia.deals.location_picker.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.deals.location_picker.di.DealsLocationScope
import com.tokopedia.deals.location_picker.domain.viewmodel.DealsLocationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DealsLocationViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DealsLocationViewModel::class)
    abstract fun provideDealsLocationViewModel(viewModel: DealsLocationViewModel): ViewModel

    @Binds
    @DealsLocationScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}