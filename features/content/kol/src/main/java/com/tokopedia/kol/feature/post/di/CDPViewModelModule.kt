package com.tokopedia.kol.feature.post.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.kol.feature.postdetail.view.viewmodel.CPDRevampViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class CDPViewModelModule {

    @KolProfileScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CPDRevampViewModel::class)
    internal abstract fun cdpRevampViewModel(cdpRevampViewModel: CPDRevampViewModel): ViewModel

}