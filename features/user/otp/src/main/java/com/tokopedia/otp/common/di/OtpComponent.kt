package com.tokopedia.otp.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.otp.notif.view.fragment.*
import com.tokopedia.otp.qrcode.view.fragment.LoginByQrFragment
import com.tokopedia.otp.qrcode.view.fragment.LoginByQrResultFragment
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.fragment.OnboardingMiscallFragment
import com.tokopedia.otp.verification.view.fragment.VerificationFragment
import com.tokopedia.otp.verification.view.fragment.VerificationMethodFragment
import dagger.Component

/**
 * Created by Ade Fulki on 09/09/20.
 */

@OtpScope
@Component(modules = [
    OtpModule::class,
    OtpViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface OtpComponent {
    fun inject(activity: VerificationActivity)
    fun inject(fragment: VerificationFragment)
    fun inject(fragment: OnboardingMiscallFragment)
    fun inject(fragment: VerificationMethodFragment)
    fun inject(fragment: ReceiverNotifFragment)
    fun inject(fragment: SettingNotifFragment)
    fun inject(fragment: ResultNotifFragment)
    fun inject(fragment: ActivePushNotifFragment)
    fun inject(fragment: InactivePushNotifFragment)
    fun inject(fragment: LoginByQrFragment)
    fun inject(fragment: LoginByQrResultFragment)
}