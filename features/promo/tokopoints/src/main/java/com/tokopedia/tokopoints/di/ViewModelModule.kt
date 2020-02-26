package com.tokopedia.tokopoints.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopoints.view.catalogdetail.CouponCatalogViewModel
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListingViewModel
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailViewModel
import com.tokopedia.tokopoints.view.couponlisting.CouponLisitingStackedViewModel
import com.tokopedia.tokopoints.view.couponlisting.StackedCouponActivtyViewModel
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @TokoPointScope
    abstract fun getfactory(tokopointViewModelFactory: ViewModelFactory) : ViewModelProvider.Factory


    @IntoMap
    @Binds
    @TokoPointScope
    @ViewModelKey(PointHistoryViewModel::class)
    abstract fun getHistoryViewModel(viewModel: PointHistoryViewModel) : ViewModel

    @IntoMap
    @Binds
    @TokoPointScope
    @ViewModelKey(CouponDetailViewModel::class)
    abstract fun getCouponDetailViewModel(viewModel: CouponDetailViewModel) : ViewModel

    @IntoMap
    @Binds
    @TokoPointScope
    @ViewModelKey(CouponLisitingStackedViewModel::class)
    abstract fun getCouponListingStackedViewModel(viewModel: CouponLisitingStackedViewModel) : ViewModel

    @IntoMap
    @Binds
    @TokoPointScope
    @ViewModelKey(StackedCouponActivtyViewModel::class)
    abstract fun getStackedCouponActivtyViewModel(viewModel: StackedCouponActivtyViewModel) : ViewModel

    @IntoMap
    @Binds
    @TokoPointScope
    @ViewModelKey(CouponCatalogViewModel::class)
    abstract fun getCouponCAtalogViewModel(viewModel: CouponCatalogViewModel) : ViewModel

    @IntoMap
    @Binds
    @TokoPointScope
    @ViewModelKey(CatalogListingViewModel::class)
    abstract fun getCatalogListingViewModel(viewModel: CatalogListingViewModel) : ViewModel

}