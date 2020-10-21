package com.tokopedia.sellerorder.list.di.bulkaccept

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.list.presentation.viewmodels.SomListBulkAcceptOrderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@SomBulkAcceptOrderScope
abstract class SomBulkAcceptOrderViewModelModule {

    @SomBulkAcceptOrderScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SomListBulkAcceptOrderViewModel::class)
    internal abstract fun somListBulkAcceptOrderViewModel(viewModel: SomListBulkAcceptOrderViewModel): ViewModel
}