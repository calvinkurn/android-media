package com.tokopedia.category.navbottomsheet.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.category.navbottomsheet.viewmodel.CategoryNavBottomViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @CategoryNavBottomSheetScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @CategoryNavBottomSheetScope
    @ViewModelKey(CategoryNavBottomViewModel::class)
    internal abstract fun bindsCategoryNavViewModel(bottomViewModel: CategoryNavBottomViewModel): ViewModel
}
