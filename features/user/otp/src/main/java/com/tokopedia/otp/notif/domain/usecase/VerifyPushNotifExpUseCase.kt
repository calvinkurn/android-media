package com.tokopedia.otp.notif.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.otp.common.abstraction.BaseOtpUseCase
import com.tokopedia.otp.notif.domain.pojo.VerifyPushNotifExpPojo
import com.tokopedia.otp.notif.domain.pojo.VerifyPushNotifPojo
import com.tokopedia.otp.notif.domain.query.VerifyPushNotifExpQuery
import com.tokopedia.otp.notif.domain.query.VerifyPushNotifQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 22/09/20.
 */

class VerifyPushNotifExpUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : BaseOtpUseCase<VerifyPushNotifExpPojo>(dispatcher) {

    fun getParams(
            challengeCode: String,
            signature: String,
            status: String
    ): Map<String, Any> = mapOf(
            PARAM_CHALLENGE_CODE to challengeCode,
            PARAM_SIGNATURE to signature,
            PARAM_STATUS to status
    )

    override suspend fun getData(parameter: Map<String, Any>): VerifyPushNotifExpPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                VerifyPushNotifExpQuery.query,
                VerifyPushNotifExpPojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_CHALLENGE_CODE = "challengeCode"
        private const val PARAM_SIGNATURE = "signature"
        private const val PARAM_STATUS = "status"
    }
}