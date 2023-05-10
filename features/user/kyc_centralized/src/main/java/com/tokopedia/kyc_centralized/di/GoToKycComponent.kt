package com.tokopedia.kyc_centralized.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.BridgingAccountLinkingFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.DobChallengeFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.FinalLoaderFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.GotoKycRouterFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.StatusSubmissionFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.OnboardBenefitFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.transparent.GotoKycTransparentFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        GotoKycViewModelModule::class,
        GoToKycModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface GoToKycComponent {
    fun inject(fragment: GotoKycTransparentFragment)
    fun inject(fragment: GotoKycRouterFragment)
    fun inject(fragment: StatusSubmissionFragment)
    fun inject(fragment: OnboardBenefitFragment)
    fun inject(fragment: DobChallengeFragment)
    fun inject(bottomSheet: OnboardProgressiveBottomSheet)
    fun inject(fragment: BridgingAccountLinkingFragment)
    fun inject(fragment: FinalLoaderFragment)
}
