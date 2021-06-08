package com.tokopedia.otp.stub.common.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.common.di.OtpScope
import com.tokopedia.otp.stub.verification.domain.usecase.*
import com.tokopedia.otp.verification.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
class OtpFakeUseCaseModule {

    @Provides
    @OtpScope
    fun provideGetVerificationMethodUseCase2FAStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers
    ): GetVerificationMethodUseCase2FAStub = GetVerificationMethodUseCase2FAStub(graphqlRepository, dispatcher)

    @Provides
    @OtpScope
    fun provideGetVerificationMethodUseCase2FA(
            stub: GetVerificationMethodUseCase2FAStub
    ): GetVerificationMethodUseCase2FA = stub

    @Provides
    @OtpScope
    fun provideGetVerificationMethodUseCaseStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers
    ): GetVerificationMethodUseCaseStub = GetVerificationMethodUseCaseStub(graphqlRepository, dispatcher)

    @Provides
    @OtpScope
    fun provideGetVerificationMethodUseCase(
            stub: GetVerificationMethodUseCaseStub
    ): GetVerificationMethodUseCase = stub

    @Provides
    @OtpScope
    fun provideOtpValidateUseCase2FAStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers
    ): OtpValidateUseCase2FAStub = OtpValidateUseCase2FAStub(graphqlRepository, dispatcher)

    @Provides
    @OtpScope
    fun provideOtpValidateUseCase2FA(
            stub: OtpValidateUseCase2FAStub
    ): OtpValidateUseCase2FA = stub

    @Provides
    @OtpScope
    fun provideOtpValidateUseCaseStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers
    ): OtpValidateUseCaseStub = OtpValidateUseCaseStub(graphqlRepository, dispatcher)

    @Provides
    @OtpScope
    fun provideOtpValidateUseCase(
            stub: OtpValidateUseCaseStub
    ): OtpValidateUseCase = stub

    @Provides
    @OtpScope
    fun provideSendOtp2FAUseCaseStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers
    ): SendOtp2FAUseCaseStub = SendOtp2FAUseCaseStub(graphqlRepository, dispatcher)

    @Provides
    @OtpScope
    fun provideSendOtp2FAUseCase(
            stub: SendOtp2FAUseCaseStub
    ): SendOtp2FAUseCase = stub

    @Provides
    @OtpScope
    fun provideSendOtpUseCaseStub(
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatchers
    ): SendOtpUseCaseStub = SendOtpUseCaseStub(graphqlRepository, dispatcher)

    @Provides
    @OtpScope
    fun provideSendOtpUseCase(
            stub: SendOtpUseCaseStub
    ): SendOtpUseCase = stub
}