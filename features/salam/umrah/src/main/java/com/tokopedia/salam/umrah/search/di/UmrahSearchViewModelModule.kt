package com.tokopedia.salam.umrah.search.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.salam.umrah.search.presentation.viewmodel.UmrahSearchFilterSortViewModel
import com.tokopedia.salam.umrah.search.presentation.viewmodel.UmrahSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 18/10/2019
 */
@Module
abstract class UmrahSearchViewModelModule {

    @UmrahSearchScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UmrahSearchViewModel::class)
    internal abstract fun umrahSearchViewModel(viewModel: UmrahSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UmrahSearchFilterSortViewModel::class)
    internal abstract fun umrahSearchFilterViewModel(viewModel: UmrahSearchFilterSortViewModel): ViewModel

}