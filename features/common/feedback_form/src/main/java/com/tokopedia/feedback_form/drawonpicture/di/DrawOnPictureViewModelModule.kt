package com.tokopedia.feedback_form.drawonpicture.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.feedback_form.drawonpicture.presentation.viewmodel.DrawOnPictureViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 01/10/2020
 */
@Module
abstract class DrawOnPictureViewModelModule {
    @DrawOnPictureScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @DrawOnPictureScope
    @Binds
    @IntoMap
    @ViewModelKey(DrawOnPictureViewModel::class)
    abstract fun drawOnPictureViewModel(viewModel: DrawOnPictureViewModel): ViewModel
}