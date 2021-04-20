package com.tokopedia.otp.verification.domain.usecase

/**
 * Created by Yoris Prayogo on 07/09/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.otp.common.abstraction.BaseOtpUseCase
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.query.OtpValidateQuery2FA
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OtpValidateUseCase2FA @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : BaseOtpUseCase<OtpValidatePojo>(dispatcher) {

    fun getParams(
            otpType: String,
            validateToken: String,
            userIdEnc: String,
            mode: String,
            code: String
    ): Map<String, Any> = mapOf(
            PARAM_OTP_TYPE to otpType,
            PARAM_VALIDATE_TOKEN_2FA to validateToken,
            PARAM_USERID_ENC to userIdEnc,
            PARAM_MODE to mode,
            PARAM_CODE to code
    )

    override suspend fun getData(parameter: Map<String, Any>): OtpValidatePojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                OtpValidateQuery2FA.query,
                OtpValidatePojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_CODE = "code"
        private const val PARAM_OTP_TYPE = "otpType"
        private const val PARAM_USERID_ENC = "UserIDEnc"
        private const val PARAM_VALIDATE_TOKEN_2FA = "ValidateToken"
        private const val PARAM_MODE = "mode"
    }
}