package com.tokopedia.officialstore.category.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.officialstore.category.presentation.viewmodel.OfficialStoreCategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@OfficialStoreCategoryScope
abstract class OfficialStoreCategoryViewModelModule {

    @OfficialStoreCategoryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OfficialStoreCategoryViewModel::class)
    internal abstract fun officialStoreCategoryViewModel(viewModel: OfficialStoreCategoryViewModel): ViewModel

}