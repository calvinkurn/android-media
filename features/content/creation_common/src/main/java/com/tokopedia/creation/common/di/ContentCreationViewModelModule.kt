package com.tokopedia.creation.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.creation.common.presentation.viewmodel.ContentCreationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Muhammad Furqan on 08/09/23
 */
@Module
abstract class ContentCreationViewModelModule {
    @ContentCreationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ContentCreationScope
    @ViewModelKey(ContentCreationViewModel::class)
    abstract fun contentCreationViewModel(viewModel: ContentCreationViewModel): ViewModel

}
