package com.tokopedia.product.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.view.viewmodel.product_detail.ProductDetailViewModel
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewViewModel
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
    @ViewModelKey(ProductDetailViewModel::class)
    internal abstract fun bindPdpViewModel(viewModel: ProductDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BsProductDetailInfoViewModel::class)
    internal abstract fun productBsProductDetail(viewModel: BsProductDetailInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewToViewViewModel::class)
    internal abstract fun viewToViewViewModel(viewModel: ViewToViewViewModel): ViewModel
}
