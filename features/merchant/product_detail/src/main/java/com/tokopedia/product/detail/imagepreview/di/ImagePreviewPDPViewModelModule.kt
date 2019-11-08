package com.tokopedia.product.detail.imagepreview.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.product.detail.imagepreview.di.ImagePreviewPDPScope
import com.tokopedia.product.detail.imagepreview.view.viewmodel.ImagePreviewPDPViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@ImagePreviewPDPScope
@Module
abstract class ImagePreviewPDPViewModelModule {

    @ImagePreviewPDPScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ImagePreviewPDPViewModel::class)
    internal abstract fun imagePreviewPDPViewModel(viewModel: ImagePreviewPDPViewModel): ViewModel

}