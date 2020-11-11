package com.tokopedia.category.navbottomsheet.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.category.navbottomsheet.CategoryNavViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
@CategoryNavBottomSheetScope
abstract class ViewModelModule {

    @CategoryNavBottomSheetScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @CategoryNavBottomSheetScope
    @ViewModelKey(CategoryNavViewModel::class)
    internal abstract fun bindsCategoryNavViewModel(viewModel: CategoryNavViewModel): ViewModel
}
