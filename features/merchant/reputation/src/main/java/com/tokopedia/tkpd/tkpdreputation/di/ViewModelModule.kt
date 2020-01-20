package com.tokopedia.tkpd.tkpdreputation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment.CreateReviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ReputationScope
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CreateReviewViewModel::class)
    internal abstract fun productInstallmentViewModel(viewModel: CreateReviewViewModel): ViewModel

    @ReputationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}