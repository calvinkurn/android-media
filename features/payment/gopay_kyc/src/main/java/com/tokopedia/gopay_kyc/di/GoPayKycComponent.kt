package com.tokopedia.gopay_kyc.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gopay_kyc.di.module.GoPayKycModule
import com.tokopedia.gopay_kyc.di.module.ViewModelModule
import com.tokopedia.gopay_kyc.presentation.activity.GoPayCameraKtpActivity
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayKycBaseCameraFragment
import dagger.Component

@GoPayKycScope
@Component(
    modules = [GoPayKycModule::class, ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface GoPayKycComponent {

    fun inject(goPayKycBaseCameraFragment: GoPayKycBaseCameraFragment)

}