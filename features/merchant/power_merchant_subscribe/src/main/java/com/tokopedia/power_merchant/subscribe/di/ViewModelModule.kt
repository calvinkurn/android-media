package com.tokopedia.power_merchant.subscribe.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PMCancellationQuestionnaireViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PmSubscribeViewModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PmTermsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @PowerMerchantSubscribeScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @PowerMerchantSubscribeScope
    @ViewModelKey(PMCancellationQuestionnaireViewModel::class)
    internal abstract fun pmCancellationQuestionnaireViewModel(viewModel: PMCancellationQuestionnaireViewModel): ViewModel

    @Binds
    @IntoMap
    @PowerMerchantSubscribeScope
    @ViewModelKey(PmTermsViewModel::class)
    internal abstract fun pmTermsViewModel(viewModel: PmTermsViewModel): ViewModel

    @Binds
    @IntoMap
    @PowerMerchantSubscribeScope
    @ViewModelKey(PmSubscribeViewModel::class)
    internal abstract fun pmSubscribeViewModel(viewModel: PmSubscribeViewModel): ViewModel
}