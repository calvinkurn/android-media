package com.tokopedia.kyc_centralized.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.UserIdentificationFormFaceFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.UserIdentificationFormFinalFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.info.UserIdentificationInfoFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.alacarte.UserIdentificationInfoSimpleFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.CameraKtpFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.UserIdentificationFormKtpFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.info.UserIdentificationInfoActivity
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
    fun inject(fragment: CameraKtpFragment)
    fun inject(fragment: UserIdentificationCameraFragment)
    fun inject(activity: UserIdentificationInfoActivity)
    fun inject(activity: UserIdentificationFormActivity)
    fun inject(fragment: UserIdentificationFormKtpFragment)
}
