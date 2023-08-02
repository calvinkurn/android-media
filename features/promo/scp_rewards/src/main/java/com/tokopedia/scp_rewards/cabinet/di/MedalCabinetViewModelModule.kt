package com.tokopedia.scp_rewards.cabinet.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.scp_rewards.cabinet.presentation.viewmodel.MedalCabinetViewModel
import com.tokopedia.scp_rewards.cabinet.presentation.viewmodel.SeeMoreMedaliViewModel
import com.tokopedia.scp_rewards.detail.presentation.viewmodel.MedalDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MedalCabinetViewModelModule {

    @MedalCabinetScope
    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MedalCabinetViewModel::class)
    abstract fun provideMedalCabinetViewModel(medalCabinetViewModel: MedalCabinetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SeeMoreMedaliViewModel::class)
    abstract fun provideSeeMoreMedalViewModel(seeMoreMedaliViewModel: SeeMoreMedaliViewModel) : ViewModel
}
