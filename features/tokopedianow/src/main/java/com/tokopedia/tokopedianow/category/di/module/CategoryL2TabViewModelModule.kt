package com.tokopedia.tokopedianow.category.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.category.di.scope.CategoryL2TabScope
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryL2TabViewModel
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.searchcategory.di.GraphqlModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [GraphqlModule::class])
abstract class CategoryL2TabViewModelModule {

    @CategoryL2TabScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @CategoryL2TabScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowCategoryL2TabViewModel::class)
    internal abstract fun categoryL2TabViewModel(viewModel: TokoNowCategoryL2TabViewModel): ViewModel

    @CategoryL2TabScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowProductRecommendationViewModel::class)
    internal abstract fun productRecommendationViewModel(viewModel: TokoNowProductRecommendationViewModel): ViewModel
}
