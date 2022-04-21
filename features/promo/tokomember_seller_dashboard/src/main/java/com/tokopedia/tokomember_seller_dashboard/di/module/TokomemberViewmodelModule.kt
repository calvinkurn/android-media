package com.tokopedia.tokomember_seller_dashboard.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokomember_seller_dashboard.di.scope.TokomemberDashScope
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashHomeViewmodel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashIntroViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashProgramViewmodel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberEligibilityViewModel
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
    @ViewModelKey(TokomemberDashCreateViewModel::class)
    abstract fun tokomemberCardViewmodel(viewModel: TokomemberDashCreateViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TokomemberDashProgramViewmodel::class)
    abstract fun tokomemberDashProgramViewmodel(viewModel: TokomemberDashProgramViewmodel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TokomemberEligibilityViewModel::class)
    abstract fun tokomemberEligibilityViewModel(viewModel: TokomemberEligibilityViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TokomemberDashHomeViewmodel::class)
    abstract fun tokomemberDashHomeViewmodel(viewModel: TokomemberDashHomeViewmodel): ViewModel
}