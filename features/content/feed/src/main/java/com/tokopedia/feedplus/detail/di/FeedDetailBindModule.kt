package com.tokopedia.feedplus.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.feedplus.detail.FeedDetailViewModel
import com.tokopedia.feedplus.detail.data.FeedDetailRepository
import com.tokopedia.feedplus.detail.data.FeedDetailRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by meyta.taliti on 07/09/23.
 */
@Module
abstract class FeedDetailBindModule {

    /**
     * ViewModel
     */
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedDetailViewModel::class)
    abstract fun getFeedDetailViewModel(viewModel: FeedDetailViewModel): ViewModel

    /**
     * Repository
     */
    @ActivityScope
    @Binds
    abstract fun bindRepository(
        feedDetailRepositoryImpl: FeedDetailRepositoryImpl
    ): FeedDetailRepository
}
