package com.tokopedia.kyc_centralized.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardNonProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveBottomSheet
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridgingAccountLinking.BridgingAccountLinkingFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.captureDocument.CaptureKycDocumentsFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.dobChallenge.DobChallengeFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.finalLoader.FinalLoaderFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.mainRouter.GotoKycRouterFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.statusSubmission.StatusSubmissionFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.onboardAccount.OnboardBenefitFragment
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
