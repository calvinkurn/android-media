package com.tokopedia.promotionstarget.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.promotionstarget.ViewModelFactory
import com.tokopedia.promotionstarget.di.scopes.PromoTargetScope
import com.tokopedia.promotionstarget.ui.viewmodel.TargetPromotionsDialogVM
import com.tokopedia.promotionstarget.ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@PromoTargetScope
abstract class ViewModelModule {

    @PromoTargetScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @PromoTargetScope
    @Binds
    @IntoMap
    @ViewModelKey(TargetPromotionsDialogVM::class)
    internal abstract fun targetPromotionsDialogVM(viewModel: TargetPromotionsDialogVM): ViewModel

}