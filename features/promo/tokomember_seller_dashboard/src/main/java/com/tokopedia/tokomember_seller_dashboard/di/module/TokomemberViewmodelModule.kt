package com.tokopedia.tokomember_seller_dashboard.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokomember_seller_dashboard.di.scope.TokomemberDashScope
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateCardViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashIntroViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashProgramViewmodel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TokomemberViewmodelModule {

    @TokomemberDashScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TokomemberDashIntroViewModel::class)
    abstract fun tokomemberDashIntroViewmodel(viewModel: TokomemberDashIntroViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TokomemberDashCreateCardViewModel::class)
    abstract fun tokomemberCardViewmodel(viewModel: TokomemberDashCreateCardViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TokomemberDashProgramViewmodel::class)
    abstract fun tokomemberDashProgramViewmodel(viewModel: TokomemberDashProgramViewmodel): ViewModel
}