package com.tokopedia.shopdiscount.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyViewModel
import com.tokopedia.shopdiscount.di.scope.ShopDiscountComponentScope
import com.tokopedia.shopdiscount.info.presentation.viewmodel.ShopDiscountSellerInfoBottomSheetViewModel
import com.tokopedia.shopdiscount.manage.presentation.container.DiscountedProductManageViewModel
import com.tokopedia.shopdiscount.manage.presentation.list.DiscountedProductListViewModel
import com.tokopedia.shopdiscount.manage_discount.presentation.view.viewmodel.ShopDiscountManageViewModel
import com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel.ShopDiscountManageProductViewModel
import com.tokopedia.shopdiscount.manage_product_discount.presentation.viewmodel.ShopDiscountManageVariantViewModel
import com.tokopedia.shopdiscount.product_detail.presentation.viewmodel.ShopDiscountProductDetailBottomSheetViewModel
import com.tokopedia.shopdiscount.search.presentation.SearchProductViewModel
import com.tokopedia.shopdiscount.select.presentation.SelectProductViewModel
import com.tokopedia.shopdiscount.set_period.presentation.viewmodel.SetPeriodBottomSheetViewModel
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
    @ViewModelKey(DiscountedProductManageViewModel::class)
    internal abstract fun provideProductManageViewModel(viewModel: DiscountedProductManageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopDiscountProductDetailBottomSheetViewModel::class)
    internal abstract fun provideShopDiscountProductDetailBottomSheetViewModel(viewModel: ShopDiscountProductDetailBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiscountedProductListViewModel::class)
    internal abstract fun provideProductListViewModel(viewModel: DiscountedProductListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopDiscountSellerInfoBottomSheetViewModel::class)
    internal abstract fun provideShopDiscountSellerInfoBottomSheetViewModel(viewModel: ShopDiscountSellerInfoBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchProductViewModel::class)
    internal abstract fun provideSearchProductViewModel(viewModel: SearchProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectProductViewModel::class)
    internal abstract fun provideSelectProductViewModel(viewModel: SelectProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopDiscountManageViewModel::class)
    internal abstract fun provideShopDiscountManageDiscountViewModel(viewModel: ShopDiscountManageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopDiscountManageProductViewModel::class)
    internal abstract fun provideShopDiscountManageProductDiscountViewModel(viewModel: ShopDiscountManageProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetPeriodBottomSheetViewModel::class)
    internal abstract fun provideSetPeriodBottomSheetViewModel(viewModel: SetPeriodBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopDiscountManageVariantViewModel::class)
    internal abstract fun provideShopDiscountManageProductVariantDiscountViewModel(viewModel: ShopDiscountManageVariantViewModel): ViewModel

}