package com.tokopedia.otp.stub.common.di

import com.tokopedia.otp.AndroidFileUtil
import com.tokopedia.otp.common.di.OtpScope
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class OtpResponseModule {

    @OtpScope
    @Named("OtpMethodSuccess")
    @Provides
    fun provideSuccessValueOtpMethod(): OtpModeListPojo = AndroidFileUtil.parse(
            "get_verification_method_success.json",
            OtpModeListPojo::class.java
    )

    @OtpScope
    @Named("OtpMethodFailed")
    @Provides
    fun provideFailedValueOtpMethod(): OtpModeListPojo = AndroidFileUtil.parse(
            "get_verification_method_failed.json",
            OtpModeListPojo::class.java
    )

    @OtpScope
    @Named("OtpMethod2FASuccess")
    @Provides
    fun provideSuccessValueOtpMethod2FA(): OtpModeListPojo = AndroidFileUtil.parse(
            "get_verification_method_2FA_success.json",
            OtpModeListPojo::class.java
    )

    @OtpScope
    @Named("OtpMethod2FAFailed")
    @Provides
    fun provideFailedValueOtpMethod2FA(): OtpModeListPojo = AndroidFileUtil.parse(
            "get_verification_method_2FA_failed.json",
            OtpModeListPojo::class.java
    )

    @OtpScope
    @Named("OtpValidateSuccess")
    @Provides
    fun provideSuccessValueOtpValidate(): OtpValidatePojo = AndroidFileUtil.parse(
            "otp_validate_success.json",
            OtpValidatePojo::class.java
    )

    @OtpScope
    @Named("OtpValidateFailed")
    @Provides
    fun provideFailedValueOtpValidate(): OtpValidatePojo = AndroidFileUtil.parse(
            "otp_validate_failed.json",
            OtpValidatePojo::class.java
    )

    @OtpScope
    @Named("OtpValidate2FASuccess")
    @Provides
    fun provideSuccessValueOtpValidate2FA(): OtpValidatePojo = AndroidFileUtil.parse(
            "otp_validate_2FA_success.json",
            OtpValidatePojo::class.java
    )

    @OtpScope
    @Named("OtpValidate2FAFailed")
    @Provides
    fun provideFailedValueOtpValidate2FA(): OtpValidatePojo = AndroidFileUtil.parse(
            "otp_validate_2FA_failed.json",
            OtpValidatePojo::class.java
    )

    @OtpScope
    @Named("SendOtpSuccess")
    @Provides
    fun provideSuccessValueSendOtp(): OtpRequestPojo = AndroidFileUtil.parse(
            "send_otp_success.json",
            OtpRequestPojo::class.java
    )

    @OtpScope
    @Named("SendOtpFailed")
    @Provides
    fun provideFailedValueSendOtp(): OtpRequestPojo = AndroidFileUtil.parse(
            "send_otp_failed.json",
            OtpRequestPojo::class.java
    )

    @OtpScope
    @Named("SendOtp2FASuccess")
    @Provides
    fun provideSuccessValueSendOtp2FA(): OtpRequestPojo = AndroidFileUtil.parse(
            "send_otp_2FA_success.json",
            OtpRequestPojo::class.java
    )

    @OtpScope
    @Named("SendOtp2FAFailed")
    @Provides
    fun provideFailedValueSendOtp2FA(): OtpRequestPojo = AndroidFileUtil.parse(
            "send_otp_2FA_failed.json",
            OtpRequestPojo::class.java
    )
}