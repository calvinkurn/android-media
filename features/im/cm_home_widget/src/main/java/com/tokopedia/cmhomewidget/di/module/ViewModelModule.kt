package com.tokopedia.cmhomewidget.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.cmhomewidget.viewmodel.DummyTestCMHomeWidgetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

// todo delete cm home widget dummy things
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DummyTestCMHomeWidgetViewModel::class)
    internal abstract fun bindsDummyTestCMHomeWidgetViewModel(viewModel: DummyTestCMHomeWidgetViewModel): ViewModel

}