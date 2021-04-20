package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.otp.common.abstraction.BaseOtpUseCase
import com.tokopedia.otp.verification.domain.data.OtpValidatePojo
import com.tokopedia.otp.verification.domain.query.OtpValidateQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 01/06/20.
 */

class OtpValidateUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : BaseOtpUseCase<OtpValidatePojo>(dispatcher) {

    @JvmOverloads
    fun getParams(
            code: String,
            otpType: String,
            msisdn: String = "",
            fpData: String = "",
            getSL: String = "",
            email: String = "",
            mode: String = "",
            signature: String = "",
            timeUnix: String = "",
            userId: Int
    ): Map<String, Any> = mapOf(
        PARAM_CODE to code,
        PARAM_OTP_TYPE to otpType,
        PARAM_MSISDN to msisdn,
        PARAM_FP_DATA to fpData,
        PARAM_GET_SL to getSL,
        PARAM_EMAIL to email,
        PARAM_MODE to mode,
        PARAM_SIGNATURE to signature,
        PARAM_TIME_UNIX to timeUnix,
        PARAM_USERID to userId
    )

    override suspend fun getData(parameter: Map<String, Any>): OtpValidatePojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                OtpValidateQuery.query,
                OtpValidatePojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_CODE = "code"
        private const val PARAM_OTP_TYPE = "otpType"
        private const val PARAM_MSISDN = "msisdn"
        private const val PARAM_FP_DATA = "fpData"
        private const val PARAM_GET_SL = "getSL"
        private const val PARAM_EMAIL = "email"
        private const val PARAM_MODE = "mode"
        private const val PARAM_SIGNATURE = "signature"
        private const val PARAM_TIME_UNIX = "timeUnix"
        private const val PARAM_USERID = "userId"
    }
}