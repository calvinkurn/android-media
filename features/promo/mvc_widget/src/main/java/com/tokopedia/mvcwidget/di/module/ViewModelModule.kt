package com.tokopedia.mvcwidget.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.mvcwidget.MvcDetailViewModel
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponViewModel
import com.tokopedia.mvcwidget.views.benefit.PromoBenefitViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MvcDetailViewModel::class)
    abstract fun getMvcViewModel(viewModel: MvcDetailViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MerchantCouponViewModel::class)
    abstract fun getMerchantCouponViewmodel(viewModel: MerchantCouponViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PromoBenefitViewModel::class)
    internal abstract fun bindPromoBenefitViewModel(viewModel: PromoBenefitViewModel): ViewModel
}
