package com.tokopedia.shopdiscount.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyViewModel
import com.tokopedia.shopdiscount.di.scope.ShopDiscountComponentScope
import com.tokopedia.shopdiscount.manage.presentation.list.ProductListViewModel
import com.tokopedia.shopdiscount.manage.presentation.container.ProductManageViewModel
import com.tokopedia.shopdiscount.product_detail.presentation.viewmodel.ShopDiscountProductDetailBottomSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopDiscountViewModelModule {

    @ShopDiscountComponentScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DiscountBulkApplyViewModel::class)
    internal abstract fun provideDiscountBulkApplyViewModel(viewModel: DiscountBulkApplyViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ProductManageViewModel::class)
    internal abstract fun provideProductManageViewModel(viewModel: ProductManageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopDiscountProductDetailBottomSheetViewModel::class)
    internal abstract fun provideShopDiscountProductDetailBottomSheetViewModel(viewModel: ShopDiscountProductDetailBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductListViewModel::class)
    internal abstract fun provideProductListViewModel(viewModel: ProductListViewModel): ViewModel
}