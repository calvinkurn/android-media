package com.tokopedia.kyc_centralized.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.gotoKyc.transparent.GotoKycTransparentFragment
import dagger.Component

@ActivityScope
@Component(
    dependencies = [BaseAppComponent::class]
)
interface GoToKycComponent {
    fun inject(fragment: GotoKycTransparentFragment)
}
