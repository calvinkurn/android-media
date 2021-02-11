package com.tokopedia.product.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @ProductDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DynamicProductDetailViewModel::class)
    internal abstract fun provideDynamicPdpViewModel(viewModel: DynamicProductDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddToCartDoneViewModel::class)
    internal abstract fun addToCartDoneViewModel(viewModel: AddToCartDoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BsProductDetailInfoViewModel::class)
    internal abstract fun productBsProductDetail(viewModel: BsProductDetailInfoViewModel): ViewModel

}