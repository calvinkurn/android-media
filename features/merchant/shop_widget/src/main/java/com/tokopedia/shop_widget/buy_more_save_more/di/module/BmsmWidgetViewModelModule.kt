package com.tokopedia.shop_widget.buy_more_save_more.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop_widget.buy_more_save_more.di.scope.BmsmWidgetScope
import com.tokopedia.shop_widget.buy_more_save_more.presentation.viewmodel.BmsmWidgetTabViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module
abstract class BmsmWidgetViewModelModule {

    @BmsmWidgetScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BmsmWidgetTabViewModel::class)
    internal abstract fun provideBmsmWidgetTabViewModel(viewModel: BmsmWidgetTabViewModel): ViewModel
}
