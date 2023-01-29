package com.tokopedia.sellerpersona.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerpersona.di.SellerPersonaScope
import com.tokopedia.sellerpersona.view.viewmodel.PersonaSharedViewModel
import com.tokopedia.sellerpersona.view.viewmodel.SelectPersonaTypeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

@Module
abstract class ViewModelModule {

    @Binds
    @SellerPersonaScope
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PersonaSharedViewModel::class)
    abstract fun providePersonaSharedViewModel(viewModel: PersonaSharedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectPersonaTypeViewModel::class)
    abstract fun provideSelectPersonaTypeViewModel(viewModel: SelectPersonaTypeViewModel): ViewModel
}