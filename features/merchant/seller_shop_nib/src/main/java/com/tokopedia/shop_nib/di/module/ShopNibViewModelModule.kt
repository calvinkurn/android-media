package com.tokopedia.shop_nib.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop_nib.di.scope.ShopNibScope
import com.tokopedia.shop_nib.presentation.landing_page.LandingPageViewModel
import com.tokopedia.shop_nib.presentation.submission.NibSubmissionViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module
abstract class ShopNibViewModelModule {

    @ShopNibScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LandingPageViewModel::class)
    internal abstract fun provideLandingPageViewModel(viewModel: LandingPageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NibSubmissionViewModel::class)
    internal abstract fun provideNibSubmissionViewModel(viewModel: NibSubmissionViewModel): ViewModel

}
