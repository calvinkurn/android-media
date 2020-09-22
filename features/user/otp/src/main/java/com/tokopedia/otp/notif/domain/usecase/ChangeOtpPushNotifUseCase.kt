package com.tokopedia.otp.notif.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.otp.common.DispatcherProvider
import com.tokopedia.otp.common.abstraction.BaseOtpUseCase
import com.tokopedia.otp.notif.domain.pojo.ChangeOtpPushNotifPojo
import com.tokopedia.otp.notif.domain.query.ChangeOtpPushNotifQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 22/09/20.
 */

class ChangeOtpPushNotifUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: DispatcherProvider
) : BaseOtpUseCase<ChangeOtpPushNotifPojo>(dispatcher) {

    fun getParams(
            status: Int
    ): Map<String, Any> = mapOf(
            PARAM_STATUS to status
    )

    override suspend fun getData(parameter: Map<String, Any>): ChangeOtpPushNotifPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                ChangeOtpPushNotifQuery.query,
                ChangeOtpPushNotifPojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_STATUS = "status"
    }
}