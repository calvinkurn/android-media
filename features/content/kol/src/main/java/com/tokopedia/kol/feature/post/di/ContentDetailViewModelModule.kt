package com.tokopedia.kol.feature.post.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailRevampViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ContentDetailViewModelModule {

    @KolProfileScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ContentDetailRevampViewModel::class)
    internal abstract fun cdpRevampViewModel(cdpRevampViewModel: ContentDetailRevampViewModel): ViewModel

}