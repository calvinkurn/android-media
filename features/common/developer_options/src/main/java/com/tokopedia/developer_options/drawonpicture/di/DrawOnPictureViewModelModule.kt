package com.tokopedia.developer_options.drawonpicture.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * @author by furqan on 01/10/2020
 */
@Module
@DrawOnPictureScope
abstract class DrawOnPictureViewModelModule {
    @DrawOnPictureScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}