package com.tokopedia.kyc_centralized.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveViewModel
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridging.BridgingAccountLinkingViewModel
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge.DobChallengeViewModel
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.submit.FinalLoaderViewModel
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.status.StatusSubmissionViewModel
import com.tokopedia.kyc_centralized.ui.gotoKyc.transparent.GotoKycTransparentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GotoKycViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GotoKycTransparentViewModel::class)
    abstract fun gotoKycTransparentViewModel(viewModel: GotoKycTransparentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnboardProgressiveViewModel::class)
    abstract fun onboardProgressiveViewModel(viewModel: OnboardProgressiveViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BridgingAccountLinkingViewModel::class)
    abstract fun bridgingAccountLinkingViewModel(viewModel: BridgingAccountLinkingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DobChallengeViewModel::class)
    abstract fun dobChallengeViewModel(viewModel: DobChallengeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FinalLoaderViewModel::class)
    abstract fun finalLoaderViewModel(viewModel: FinalLoaderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatusSubmissionViewModel::class)
    abstract fun statusSubmissionViewModel(viewModel: StatusSubmissionViewModel): ViewModel

    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}
