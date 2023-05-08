package com.tokopedia.mvc.di.module

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.mvc.presentation.bottomsheet.changequota.ChangeQuotaBottomSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CommonViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChangeQuotaBottomSheetViewModel::class)
    internal abstract fun provideChangeQuotaBottomSheetViewModel(viewModel: ChangeQuotaBottomSheetViewModel): ViewModel
}
