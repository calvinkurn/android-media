package com.tokopedia.layanan_finansial.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.layanan_finansial.view.viewModel.LayananFinansialViewModel
import dagger.Binds

abstract class ViewModelModule {
    @Binds
    @LayananScope
    abstract fun getfactory(tokopointViewModelFactory: ViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @ViewModelKey(LayananFinansialViewModel::class)
    @LayananScope
    abstract fun getViewModel(model: LayananFinansialViewModel) : ViewModel
}