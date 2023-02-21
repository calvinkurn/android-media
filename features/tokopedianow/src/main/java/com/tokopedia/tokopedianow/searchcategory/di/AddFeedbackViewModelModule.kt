package com.tokopedia.tokopedianow.searchcategory.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.AddProductFeedbackUseCaseModule
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.AddFeedbackViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(
    includes = [AddProductFeedbackUseCaseModule::class]
)
abstract class AddFeedbackViewModelModule {
    @SearchCategoryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddFeedbackViewModel::class)
    internal abstract fun getFeedbackViewModel(viewModel:AddFeedbackViewModel) : ViewModel
}
