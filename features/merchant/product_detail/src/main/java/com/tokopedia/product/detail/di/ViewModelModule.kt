package com.tokopedia.product.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.DynamicProductDetailViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.base.SubViewModelScope
import com.tokopedia.product.detail.view.viewmodel.product_detail.base.SubViewModelScopeProvider
import com.tokopedia.product.detail.view.viewmodel.product_detail.base.SubViewModelScopeProviderImpl
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @ProductDetailScope
    @Binds
    internal abstract fun bindSubViewModelScopeProvider(
        provider: SubViewModelScopeProviderImpl
    ): SubViewModelScopeProvider

    @ProductDetailScope
    @Binds
    internal abstract fun bindSubViewModelScope(
        provider: SubViewModelScopeProvider
    ): SubViewModelScope

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

    @Binds
    @IntoMap
    @ViewModelKey(ViewToViewViewModel::class)
    internal abstract fun viewToViewViewModel(viewModel: ViewToViewViewModel): ViewModel
}
