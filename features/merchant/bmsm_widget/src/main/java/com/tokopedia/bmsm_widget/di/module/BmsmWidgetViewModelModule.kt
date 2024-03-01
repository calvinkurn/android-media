package com.tokopedia.bmsm_widget.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.bmsm_widget.di.scope.BmsmWidgetScope
import com.tokopedia.bmsm_widget.presentation.bottomsheet.GiftListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BmsmWidgetViewModelModule {

    @BmsmWidgetScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
    @Binds
    @IntoMap
    @ViewModelKey(GiftListViewModel::class)
    internal abstract fun provideGiftListViewModel(viewModel: GiftListViewModel): ViewModel
}
