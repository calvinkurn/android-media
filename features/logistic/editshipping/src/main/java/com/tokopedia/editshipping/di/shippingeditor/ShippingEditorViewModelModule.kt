package com.tokopedia.editshipping.di.shippingeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.editshipping.ui.shippingeditor.ShippingEditorViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShippingEditorViewModelModule {
    @ShippingEditorScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ShippingEditorScope
    @Binds
    @IntoMap
    @ViewModelKey(ShippingEditorViewModel::class)
    internal abstract fun providesShippingEditorViewModel(viewModel: ShippingEditorViewModel): ViewModel
}