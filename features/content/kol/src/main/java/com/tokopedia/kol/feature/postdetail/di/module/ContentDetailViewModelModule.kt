package com.tokopedia.kol.feature.postdetail.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.kol.feature.postdetail.di.ContentDetailScope
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ContentDetailViewModelModule {

    @ContentDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ContentDetailScope
    @Binds
    @IntoMap
    @ViewModelKey(ContentDetailViewModel::class)
    internal abstract fun bindViewModel(viewModel: ContentDetailViewModel): ViewModel

}