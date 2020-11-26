package com.tokopedia.otp.verification.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.otp.verification.common.DispatcherProvider
import com.tokopedia.otp.verification.domain.data.OtpModeListPojo
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.query.OtpModeListQuery
import com.tokopedia.otp.verification.domain.query.OtpRequestQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 01/06/20.
 */

class SendOtpUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: DispatcherProvider
) : BaseVerificationUseCase<OtpRequestPojo>(dispatcher) {

    @JvmOverloads
    fun getParams(
            otpType: String,
            mode: String,
            msisdn: String = "",
            email: String = "",
            otpDigit: Int = 6
    ): Map<String, Any> = mapOf(
        PARAM_OTP_TYPE to otpType,
        PARAM_MODE to mode,
        PARAM_MSISDN to msisdn,
        PARAM_EMAIL to email,
        PARAM_OTP_DIGIT to otpDigit
    )

    override suspend fun getData(parameter: Map<String, Any>): OtpRequestPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                OtpRequestQuery.query,
                OtpRequestPojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_OTP_TYPE = "otpType"
        private const val PARAM_MODE = "mode"
        private const val PARAM_MSISDN = "msisdn"
        private const val PARAM_EMAIL = "email"
        private const val PARAM_OTP_DIGIT = "otpDigit"
    }
}