package com.tokopedia.tokopoints.di

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailViewModel
import com.tokopedia.tokopoints.view.viewmodel.PointHistoryViewModel
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

}