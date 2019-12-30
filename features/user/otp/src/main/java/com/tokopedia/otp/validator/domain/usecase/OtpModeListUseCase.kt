package com.tokopedia.otp.validator.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.otp.validator.data.OtpModeListPojo
import com.tokopedia.otp.validator.data.OtpParams
import com.tokopedia.otp.validator.di.ValidatorQueryConstant
import javax.inject.Inject
import javax.inject.Named

/**
 * @author rival
 * @created on 9/12/2019
 */

class OtpModeListUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(ValidatorQueryConstant.QUERY_OTP_MODE_LIST)
        private val query: String
) : ValidatorUseCase<OtpModeListPojo>() {

    override suspend fun executeOnBackground(): OtpModeListPojo {
        val request = GraphqlRequest(query, OtpModeListPojo::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.executeOnBackground().run {
            getData<OtpModeListPojo>(OtpModeListPojo::class.java)
        }
    }

    companion object {
        fun createRequestParams(otpParams: OtpParams): Map<String, Any> {
            return mapOf(
                    ValidatorQueryConstant.PARAM_OTP_TYPE to otpParams.otpType.toString(),
                    ValidatorQueryConstant.PARAM_USER_ID to otpParams.userId,
                    ValidatorQueryConstant.PARAM_MSISDN to otpParams.msisdn,
                    ValidatorQueryConstant.PARAM_EMAIL to otpParams.email
            )
        }
    }
}