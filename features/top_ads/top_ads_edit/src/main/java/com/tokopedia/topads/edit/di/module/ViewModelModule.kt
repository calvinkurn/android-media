package com.tokopedia.topads.edit.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.edit.di.EditAdScope
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.topads.edit.view.model.KeywordAdsViewModel
import com.tokopedia.topads.edit.view.model.ProductAdsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Pika on 7/4/20.
 */


@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EditFormDefaultViewModel::class)
    internal abstract fun provideEditFormDefaultViewModel(viewModel: EditFormDefaultViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(KeywordAdsViewModel::class)
    internal abstract fun provideKeywordAdsViewModel(viewModel: KeywordAdsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductAdsListViewModel::class)
    internal abstract fun provideProductAdsListViewModel(viewModel: ProductAdsListViewModel): ViewModel
}


