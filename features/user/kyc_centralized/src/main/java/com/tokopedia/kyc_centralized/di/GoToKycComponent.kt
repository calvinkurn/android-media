package com.tokopedia.kyc_centralized.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.RouterFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.StatusSubmissionFragment
import com.tokopedia.kyc_centralized.ui.gotoKyc.onboardbenefit.GotoKycOnboardBenefitFragment
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
    fun inject(fragment: RouterFragment)
    fun inject(fragment: StatusSubmissionFragment)
    fun inject(fragment: GotoKycOnboardBenefitFragment)
}
