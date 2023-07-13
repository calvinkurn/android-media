package com.tokopedia.tokofood.stub.purchase.promo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.stub.purchase.promo.presentation.viewmodel.TokoFoodPromoViewModelOldStub
import com.tokopedia.tokofood.stub.purchase.promo.presentation.viewmodel.TokoFoodPromoViewModelStub
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokoFoodPromoViewModelModuleStub {

    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPromoViewModelStub::class)
    internal abstract fun bindViewModel(viewModel: TokoFoodPromoViewModelStub): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(TokoFoodPromoViewModelOldStub::class)
    internal abstract fun bindViewModelOld(viewModel: TokoFoodPromoViewModelOldStub): ViewModel

}
