package com.tokopedia.gopay.kyc.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gopay.kyc.di.module.GoPayKycModule
import com.tokopedia.gopay.kyc.di.module.NetworkModule
import com.tokopedia.gopay.kyc.di.module.ViewModelModule
import com.tokopedia.gopay.kyc.presentation.activity.GoPayKtpInstructionActivity
import com.tokopedia.gopay.kyc.presentation.activity.GoPayReviewResultActivity
import com.tokopedia.gopay.kyc.presentation.bottomsheet.GoPayKycUploadFailedBottomSheet
import com.tokopedia.gopay.kyc.presentation.fragment.base.GoPayKycBaseCameraFragment
import com.tokopedia.gopay.kyc.presentation.fragment.GoPayPlusKycBenefitFragment
import com.tokopedia.gopay.kyc.presentation.fragment.GoPayReviewAndUploadFragment
import dagger.Component

@GoPayKycScope
@Component(
    modules = [GoPayKycModule::class, NetworkModule::class, ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface GoPayKycComponent {

    fun inject(goPayKycBaseCameraFragment: GoPayKycBaseCameraFragment)
    fun inject(goPayReviewActivity: GoPayReviewResultActivity)
    fun inject(goPayKycUploadFailedBottomSheet: GoPayKycUploadFailedBottomSheet)
    fun inject(goPayReviewAndUploadFragment: GoPayReviewAndUploadFragment)
    fun inject(goPayPlusKycBenefitFragment: GoPayPlusKycBenefitFragment)
    fun inject(goPayKtpInstructionActivity: GoPayKtpInstructionActivity)

}