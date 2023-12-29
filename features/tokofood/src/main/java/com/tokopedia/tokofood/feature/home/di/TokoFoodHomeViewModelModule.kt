package com.tokopedia.tokofood.feature.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodCategoryViewModel
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoFoodHomeViewModelModule {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodHomeViewModel::class)
    internal abstract fun bindViewModelHome(viewModel: TokoFoodHomeViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodCategoryViewModel::class)
    internal abstract fun bindViewModelCategory(viewModel: TokoFoodCategoryViewModel): ViewModel

}
