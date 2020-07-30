package com.tokopedia.otp.validator.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.validator.data.OtpModeListPojo
import com.tokopedia.otp.validator.data.OtpRequestPojo
import com.tokopedia.otp.validator.data.OtpValidatePojo
import com.tokopedia.otp.validator.domain.usecase.OtpModeListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-10-21.
 * ade.hadian@tokopedia.com
 */

@ValidatorScope
@Module
class ValidatorUseCaseModule {

    @Provides
    fun provideOtpModeListGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<OtpModeListPojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideOtpValidateGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<OtpValidatePojo> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideOtpRequestGraphQlUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<OtpRequestPojo> = GraphqlUseCase(graphqlRepository)
}