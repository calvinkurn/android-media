package com.tokopedia.kyc_centralized.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardNonProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridging.BridgingAccountLinkingFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.capture.CaptureKycDocumentsFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge.DobChallengeFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.submit.FinalLoaderFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.router.GotoKycRouterFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.status.StatusSubmissionFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.onboard.OnboardBenefitFragment
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
    fun inject(bottomSheet: OnboardNonProgressiveBottomSheet)
    fun inject(fragment: BridgingAccountLinkingFragment)
    fun inject(fragment: FinalLoaderFragment)
    fun inject(fragment: CaptureKycDocumentsFragment)
}
