package com.tokopedia.tkpd.tkpdreputation.createreputation.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment.CreateReviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@CreateReviewScope
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CreateReviewViewModel::class)
    internal abstract fun productInstallmentViewModel(viewModel: CreateReviewViewModel): ViewModel

    @CreateReviewScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}