package com.tokopedia.privacycenter.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.consentwithdrawal.ui.ConsentWithdrawalFragment
import com.tokopedia.privacycenter.main.PrivacyCenterFragment
import com.tokopedia.privacycenter.main.section.privacypolicy.PrivacyPolicySectionBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [
    PrivacyCenterModule::class,
    PrivacyCenterViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface PrivacyCenterComponent {
    fun inject(fragment: PrivacyCenterFragment)
    fun inject(fragment: ConsentWithdrawalFragment)

    fun inject(bottomSheet: PrivacyPolicySectionBottomSheet)
}
