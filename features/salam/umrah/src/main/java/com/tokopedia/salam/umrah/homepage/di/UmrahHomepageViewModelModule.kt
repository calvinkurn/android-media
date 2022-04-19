package com.tokopedia.salam.umrah.homepage.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.salam.umrah.homepage.presentation.viewmodel.UmrahHomepageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 14/10/2019
 */
@Module
abstract class UmrahHomepageViewModelModule {

    @UmrahHomepageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UmrahHomepageViewModel::class)
    internal abstract fun umrahHomepageViewModel(viewModel: UmrahHomepageViewModel): ViewModel

}