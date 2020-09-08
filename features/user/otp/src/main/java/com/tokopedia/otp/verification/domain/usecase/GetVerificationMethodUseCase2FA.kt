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
import com.tokopedia.otp.verification.common.DispatcherProvider
import com.tokopedia.otp.verification.domain.data.OtpModeListPojo
import com.tokopedia.otp.verification.domain.query.OtpModeListQuery
import com.tokopedia.otp.verification.domain.query.OtpModeListQuery2FA
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 01/06/20.
 */

class GetVerificationMethodUseCase2FA @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: DispatcherProvider
) : BaseVerificationUseCase<OtpModeListPojo>(dispatcher) {

    @JvmOverloads
    fun getParams2FA(
            otpType: String,
            validateToken: String,
            userIdEnc: String = ""
    ): Map<String, Any> = mapOf(
            PARAM_OTP_TYPE to otpType,
            PARAM_VALIDATE_TOKEN to validateToken,
            PARAM_USERID_ENC to userIdEnc
    )

    override suspend fun getData(parameter: Map<String, Any>): OtpModeListPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                OtpModeListQuery2FA.query,
                OtpModeListPojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_OTP_TYPE = "otpType"
        private const val PARAM_VALIDATE_TOKEN = "ValidateToken"
        private const val PARAM_USERID_ENC = "UserIDEnc"

    }
}