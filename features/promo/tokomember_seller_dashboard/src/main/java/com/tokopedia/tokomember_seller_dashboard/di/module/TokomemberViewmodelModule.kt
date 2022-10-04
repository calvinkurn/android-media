package com.tokopedia.tokomember_seller_dashboard.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokomember_seller_dashboard.di.scope.TokomemberDashScope
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.*
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
    @ViewModelKey(TmDashIntroViewModel::class)
    abstract fun tokomemberDashIntroViewmodel(viewModel: TmDashIntroViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TmDashCreateViewModel::class)
    abstract fun tokomemberCardViewmodel(viewModel: TmDashCreateViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TmEligibilityViewModel::class)
    abstract fun tokomemberEligibilityViewModel(viewModel: TmEligibilityViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TokomemberDashHomeViewmodel::class)
    abstract fun tokomemberDashHomeViewmodel(viewModel: TokomemberDashHomeViewmodel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TmProgramListViewModel::class)
    abstract fun tokomemberProgramListViewModel(viewModel: TmProgramListViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TmCouponViewModel::class)
    abstract fun tmCouponViewModel(viewModel: TmCouponViewModel): ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TmMemberListViewModel::class)
    abstract fun tmMemberListViewModel(viewModel : TmMemberListViewModel) : ViewModel

    @TokomemberDashScope
    @Binds
    @IntoMap
    @ViewModelKey(TmCouponDetailViewModel::class)
    abstract fun tmCouponDetailViewModel(viewModel: TmCouponDetailViewModel): ViewModel
}
