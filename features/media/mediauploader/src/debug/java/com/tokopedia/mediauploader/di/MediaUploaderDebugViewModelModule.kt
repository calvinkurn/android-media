package com.tokopedia.mediauploader.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.mediauploader.DebugMediaUploaderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MediaUploaderDebugViewModelModule {

    @Binds
    @MediaUploaderTestScope
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @MediaUploaderTestScope
    @ViewModelKey(DebugMediaUploaderViewModel::class)
    internal abstract fun getMediaUploaderStateManager(viewModel: DebugMediaUploaderViewModel): ViewModel
}
