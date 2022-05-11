package com.tokopedia.home_account.explicitprofile.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.explicitprofile.features.ExplicitProfileSharedViewModel
import com.tokopedia.home_account.explicitprofile.features.ExplicitProfileViewModel
import com.tokopedia.home_account.explicitprofile.features.categories.CategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ExplicitProfileViewModelModules {

    @ActivityScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ExplicitProfileSharedViewModel::class)
    abstract fun explicitProfileSharedViewModel(viewModel: ExplicitProfileSharedViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(ExplicitProfileViewModel::class)
    abstract fun explicitProfileViewModel(viewModel: ExplicitProfileViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun categoryViewModel(viewModel: CategoryViewModel): ViewModel

}