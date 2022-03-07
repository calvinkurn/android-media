package com.tokopedia.buyerorder.recharge.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buyerorder.recharge.presentation.viewmodel.RechargeOrderDetailViewModel
import com.tokopedia.digital.digital_recommendation.di.DigitalRecommendationViewModelModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 28/10/2021
 */
@Module(includes = [DigitalRecommendationViewModelModule::class])
abstract class RechargeOrderDetailViewModelModule {
    @Binds
    @RechargeOrderDetailScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RechargeOrderDetailViewModel::class)
    internal abstract fun provideRechargeOrderDetailViewModel(viewModel: RechargeOrderDetailViewModel): ViewModel
}