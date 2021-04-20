package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.otp.common.abstraction.BaseOtpUseCase
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.query.OtpModeListQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 01/06/20.
 */

class GetVerificationMethodUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : BaseOtpUseCase<OtpModeListPojo>(dispatcher) {

    @JvmOverloads
    fun getParams(
            otpType: String,
            userId: String,
            msisdn: String = "",
            email: String = ""
    ): Map<String, Any> = mapOf(
            PARAM_OTP_TYPE to otpType,
            PARAM_USERID to userId,
            PARAM_MSISDN to msisdn,
            PARAM_EMAIL to email
    )

    override suspend fun getData(parameter: Map<String, Any>): OtpModeListPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                OtpModeListQuery.query,
                OtpModeListPojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_OTP_TYPE = "otpType"
        private const val PARAM_USERID = "userId"
        private const val PARAM_MSISDN = "msisdn"
        private const val PARAM_EMAIL = "email"
    }
}