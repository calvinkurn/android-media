package com.tokopedia.buyerorderdetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailExtensionViewModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.OwocViewModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.PartialOrderFulfillmentViewModel
import com.tokopedia.digital.digital_recommendation.di.DigitalRecommendationViewModelModule
import com.tokopedia.scp_rewards_touchpoints.touchpoints.viewmodel.ScpRewardsMedalTouchPointViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [DigitalRecommendationViewModelModule::class])
abstract class BuyerOrderDetailViewModelModule {
    @Binds
    @BuyerOrderDetailScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BuyerOrderDetailViewModel::class)
    internal abstract fun provideBuyerOrderDetailViewModel(viewModel: BuyerOrderDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BuyerOrderDetailExtensionViewModel::class)
    internal abstract fun provideOrderDetailExtensionViewModel(viewModel: BuyerOrderDetailExtensionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PartialOrderFulfillmentViewModel::class)
    internal abstract fun providePartialOrderFulfillmentViewModel(viewModel: PartialOrderFulfillmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScpRewardsMedalTouchPointViewModel::class)
    internal abstract fun provideScpRewardsMedalTouchPointViewModel(viewModel: ScpRewardsMedalTouchPointViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OwocViewModel::class)
    internal abstract fun provideOwocViewModel(viewModel: OwocViewModel): ViewModel
}
