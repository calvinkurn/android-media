package com.tokopedia.officialstore.category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.officialstore.category.presentation.viewmodel.OfficialStoreCategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OfficialStoreCategoryViewModelModule {

    @OfficialStoreCategoryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OfficialStoreCategoryViewModel::class)
    internal abstract fun officialStoreCategoryViewModel(viewModel: OfficialStoreCategoryViewModel): ViewModel

}