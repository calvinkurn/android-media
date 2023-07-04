package com.tokopedia.scp_rewards_touchpoints.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.viewmodel.MedalCelebrationViewModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.viewmodel.ScpRewardsMedaliTouchPointViewModel
import com.tokopedia.scp_rewards_touchpoints.view.bottomsheet.di.CelebrationScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @CelebrationScope
    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MedalCelebrationViewModel::class)
    abstract fun provideMedalCelebrationViewModel(medalCelebrationViewModel: MedalCelebrationViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScpRewardsMedaliTouchPointViewModel::class)
    abstract fun provideScpToasterViewModel(scpToasterViewModel: ScpRewardsMedaliTouchPointViewModel) : ViewModel
}
