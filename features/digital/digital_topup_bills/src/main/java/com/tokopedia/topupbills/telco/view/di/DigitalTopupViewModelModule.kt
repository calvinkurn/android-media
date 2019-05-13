package com.tokopedia.topupbills.telco.view.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Module
@DigitalTopupScope
abstract class DigitalTopupViewModelModule {

    @DigitalTopupScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalTelcoViewModel::class)
    internal abstract fun digitalTelcoViewModel(viewModel: DigitalTelcoViewModel): ViewModel
}