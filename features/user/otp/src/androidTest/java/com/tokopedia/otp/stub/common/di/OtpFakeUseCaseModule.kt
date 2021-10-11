package com.tokopedia.otp.stub.common.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.common.di.OtpScope
import com.tokopedia.otp.stub.verification.domain.usecase.*
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class OtpFakeUseCaseModule {

    @Provides
    @OtpScope
    fun provideGetVerificationMethodUseCase2FAStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers,
            @Named("OtpMethod2FASuccess") otpModeListPojo: OtpModeListPojo
    ): GetVerificationMethodUseCase2FAStub {
        val usecase = GetVerificationMethodUseCase2FAStub(graphqlRepository, dispatcher)
        usecase.response = otpModeListPojo
        return usecase
    }

    @Provides
    @OtpScope
    fun provideGetVerificationMethodUseCase2FA(
            stub: GetVerificationMethodUseCase2FAStub
    ): GetVerificationMethodUseCase2FA = stub

    @Provides
    @OtpScope
    fun provideGetVerificationMethodUseCaseStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers,
            @Named("OtpMethodSuccess") otpModeListPojo: OtpModeListPojo
    ): GetVerificationMethodUseCaseStub {
        val usecase = GetVerificationMethodUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = otpModeListPojo
        return usecase
    }

    @Provides
    @OtpScope
    fun provideGetVerificationMethodUseCase(
            stub: GetVerificationMethodUseCaseStub
    ): GetVerificationMethodUseCase = stub

    @Provides
    @OtpScope
    fun provideOtpValidateUseCase2FAStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers,
            @Named("OtpValidate2FASuccess") otpValidatePojo: OtpValidatePojo
    ): OtpValidateUseCase2FAStub {
        val usecase = OtpValidateUseCase2FAStub(graphqlRepository, dispatcher)
        usecase.response = otpValidatePojo
        return usecase
    }

    @Provides
    @OtpScope
    fun provideOtpValidateUseCase2FA(
            stub: OtpValidateUseCase2FAStub
    ): OtpValidateUseCase2FA = stub

    @Provides
    @OtpScope
    fun provideOtpValidateUseCaseStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers,
            @Named("OtpValidateSuccess") otpValidatePojo: OtpValidatePojo
    ): OtpValidateUseCaseStub {
        val usecase = OtpValidateUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = otpValidatePojo
        return usecase
    }

    @Provides
    @OtpScope
    fun provideOtpValidateUseCase(
            stub: OtpValidateUseCaseStub
    ): OtpValidateUseCase = stub

    @Provides
    @OtpScope
    fun provideSendOtp2FAUseCaseStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers,
            @Named("SendOtp2FASuccess") otpRequestPojo: OtpRequestPojo
    ): SendOtp2FAUseCaseStub {
        val usecase = SendOtp2FAUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = otpRequestPojo
        return usecase
    }

    @Provides
    @OtpScope
    fun provideSendOtp2FAUseCase(
            stub: SendOtp2FAUseCaseStub
    ): SendOtp2FAUseCase = stub

    @Provides
    @OtpScope
    fun provideSendOtpUseCaseStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers,
            @Named("SendOtpSuccess") otpRequestPojo: OtpRequestPojo
    ): SendOtpUseCaseStub {
        val usecase = SendOtpUseCaseStub(graphqlRepository, dispatcher)
        usecase.response = otpRequestPojo
        return usecase
    }

    @Provides
    @OtpScope
    fun provideSendOtpUseCase(
            stub: SendOtpUseCaseStub
    ): SendOtpUseCase = stub
}