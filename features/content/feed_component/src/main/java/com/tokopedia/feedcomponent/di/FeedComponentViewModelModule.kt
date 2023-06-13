package com.tokopedia.feedcomponent.di

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.feedcomponent.presentation.viewmodel.FeedProductItemInfoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Muhammad Furqan on 21/10/22
 */
@Module
abstract class FeedComponentViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FeedProductItemInfoViewModel::class)
    abstract fun feedProductItemInfoViewModel(viewModel: FeedProductItemInfoViewModel): ViewModel

}
