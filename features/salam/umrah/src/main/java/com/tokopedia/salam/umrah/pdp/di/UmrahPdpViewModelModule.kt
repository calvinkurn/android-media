package com.tokopedia.salam.umrah.pdp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.salam.umrah.pdp.presentation.viewmodel.UmrahPdpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
/**
 * @author by M on 30/10/19
 */
@Module
abstract class UmrahPdpViewModelModule {

    @UmrahPdpScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UmrahPdpViewModel::class)
    internal abstract fun umrahPdpViewModel(viewModel: UmrahPdpViewModel): ViewModel
}