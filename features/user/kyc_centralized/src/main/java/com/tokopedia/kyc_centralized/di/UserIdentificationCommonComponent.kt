package com.tokopedia.kyc_centralized.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.cKyc.form.UserIdentificationFormFaceFragment
import com.tokopedia.kyc_centralized.ui.cKyc.form.UserIdentificationFormFinalFragment
import com.tokopedia.kyc_centralized.ui.cKyc.info.UserIdentificationInfoFragment
import com.tokopedia.kyc_centralized.ui.cKyc.alacarte.UserIdentificationInfoSimpleFragment
import dagger.Component

@ActivityScope
@Component(modules = [
    UserIdentificationCommonModule::class,
    KycCentralizedViewModelModule::class,
    KycUploadImageModule::class],
    dependencies = [BaseAppComponent::class]
)
interface UserIdentificationCommonComponent {
    fun inject(fragment: UserIdentificationFormFaceFragment?)
    fun inject(fragment: UserIdentificationFormFinalFragment?)
    fun inject(fragment: UserIdentificationInfoFragment?)
    fun inject(fragment: UserIdentificationInfoSimpleFragment?)
}
