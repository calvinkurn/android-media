package com.tokopedia.sellerfeedback.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerfeedback.di.scope.SellerFeedbackScope
import com.tokopedia.sellerfeedback.presentation.viewmodel.SellerFeedbackViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SellerFeedbackViewModelModule {

    @SellerFeedbackScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerFeedbackViewModel::class)
    abstract fun sellerFeedbackViewModel(viewModel: SellerFeedbackViewModel): ViewModel
}