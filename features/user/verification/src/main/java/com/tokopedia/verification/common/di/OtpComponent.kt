package com.tokopedia.verification.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.verification.notif.view.fragment.*
import com.tokopedia.verification.qrcode.view.fragment.LoginByQrFragment
import com.tokopedia.verification.qrcode.view.fragment.LoginByQrResultFragment
import com.tokopedia.verification.otp.view.activity.VerificationActivity
import com.tokopedia.verification.otp.view.fragment.*
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
    fun inject(fragment: SmsVerificationFragment)
    fun inject(fragment: VerificationMethodFragment)
    fun inject(fragment: ReceiverNotifFragment)
    fun inject(fragment: SettingNotifFragment)
    fun inject(fragment: ResultNotifFragment)
    fun inject(fragment: ActivePushNotifFragment)
    fun inject(fragment: InactivePushNotifFragment)
    fun inject(fragment: LoginByQrFragment)
    fun inject(fragment: LoginByQrResultFragment)
    fun inject(fragment: WhatsappNotRegisteredFragment)
}
