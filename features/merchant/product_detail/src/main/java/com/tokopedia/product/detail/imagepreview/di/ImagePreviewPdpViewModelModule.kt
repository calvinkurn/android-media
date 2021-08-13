package com.tokopedia.product.detail.imagepreview.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPdpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ImagePreviewPdpViewModelModule {

    @ImagePreviewPdpScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ImagePreviewPdpViewModel::class)
    internal abstract fun imagePreviewPDPViewModel(viewModel: ImagePreviewPdpViewModel): ViewModel

}