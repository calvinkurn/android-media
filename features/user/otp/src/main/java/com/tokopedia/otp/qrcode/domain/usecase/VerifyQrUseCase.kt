package com.tokopedia.otp.qrcode.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.otp.common.abstraction.BaseOtpUseCase
import com.tokopedia.otp.qrcode.domain.pojo.VerifyQrPojo
import com.tokopedia.otp.qrcode.domain.query.VerifyQrQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VerifyQrUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : BaseOtpUseCase<VerifyQrPojo>(dispatcher) {

    fun getParams(
            data: String,
            status: String,
            signature: String
    ): Map<String, Any> = mapOf(
            PARAM_STATUS to status,
            PARAM_DATA to data,
            PARAM_SIGNATURE to signature
    )

    override suspend fun getData(parameter: Map<String, Any>): VerifyQrPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                VerifyQrQuery.query,
                VerifyQrPojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_DATA = "data"
        private const val PARAM_STATUS = "status"
        private const val PARAM_SIGNATURE = "signature"
    }
}