package com.tokopedia.feedplus.browse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.feedplus.browse.presentation.FeedBrowseViewModel
import com.tokopedia.feedplus.browse.presentation.FeedCategoryInspirationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by meyta.taliti on 11/08/23.
 */
@Module
internal abstract class FeedBrowseViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedBrowseViewModel::class)
    abstract fun getFeedBrowseViewModel(viewModel: FeedBrowseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedCategoryInspirationViewModel::class)
    abstract fun getFeedCategoryInspirationViewModel(viewModel: FeedCategoryInspirationViewModel): ViewModel
}
