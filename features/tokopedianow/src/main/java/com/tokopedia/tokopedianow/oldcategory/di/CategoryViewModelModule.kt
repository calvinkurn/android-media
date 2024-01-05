package com.tokopedia.tokopedianow.oldcategory.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowProductRecommendationViewModel
import com.tokopedia.tokopedianow.oldcategory.domain.usecase.GetCategoryUseCaseModule
import com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel.TokoNowCategoryViewModel
import com.tokopedia.tokopedianow.searchcategory.di.GraphqlModule
import com.tokopedia.tokopedianow.searchcategory.di.RemoteConfigModule
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetProductCountUseCaseModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [
    GetCategoryUseCaseModule::class,
    GetProductCountUseCaseModule::class,
    GraphqlModule::class,
    RemoteConfigModule::class
])
abstract class CategoryViewModelModule {

    @CategoryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @CategoryScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoNowCategoryViewModel::class)
    internal abstract fun categoryViewModel(viewModelTokoNow: TokoNowCategoryViewModel): ViewModel

    @Binds
    @CategoryScope
    @IntoMap
    @ViewModelKey(TokoNowProductRecommendationViewModel::class)
    internal abstract fun tokoNowProductRecommendationViewModel(viewModel: TokoNowProductRecommendationViewModel): ViewModel
}
