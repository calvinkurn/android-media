package com.tokopedia.troubleshooter.notification.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.troubleshooter.notification.di.TroubleshootScope
import com.tokopedia.troubleshooter.notification.ui.viewmodel.TroubleshootViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class TroubleshootViewModelModule {

    @Binds
    @TroubleshootScope
    internal abstract fun bindViewModelFactory(
            viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @TroubleshootScope
    @ViewModelKey(TroubleshootViewModel::class)
    internal abstract fun bindSettingStateViewModel(
            viewModel: TroubleshootViewModel
    ): ViewModel

}