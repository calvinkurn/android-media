package com.tokopedia.promotionstarget.data.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.CmGratificationViewModel
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.TargetPromotionsDialogViewModel
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.ViewModelFactory
import com.tokopedia.promotionstarget.presentation.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TargetPromotionsDialogViewModel::class)
    internal abstract fun targetPromotionsDialogVM(viewModel: TargetPromotionsDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CmGratificationViewModel::class)
    internal abstract fun cmGratificationVM(viewModel: CmGratificationViewModel): ViewModel

}