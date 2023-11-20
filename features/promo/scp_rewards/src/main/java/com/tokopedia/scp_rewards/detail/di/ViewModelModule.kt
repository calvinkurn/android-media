package com.tokopedia.scp_rewards.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.CouponListViewModel
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MedalDetailViewModelModule {

    @MedalDetailScope
    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MedalDetailViewModel::class)
    abstract fun provideMedalDetailViewModel(medalDetailViewModel: MedalDetailViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CouponListViewModel::class)
    abstract fun provideCouponListViewModel(couponListViewModel: CouponListViewModel): ViewModel
}
