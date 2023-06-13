package com.tokopedia.csat_rating.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.csat_rating.di.scope.CsatScope
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CsatRatingViewModelModule {
    @CsatScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BaseProvideRatingFragmentViewModel::class)
    internal abstract fun baseProvideRatingViewModel(viewModel: BaseProvideRatingFragmentViewModel): ViewModel
}