package com.tokopedia.otp.notif.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.otp.common.abstraction.BaseOtpUseCase
import com.tokopedia.otp.notif.domain.pojo.ChangeStatusPushNotifPojo
import com.tokopedia.otp.notif.domain.query.ChangeStatusPushNotifQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 22/09/20.
 */

class ChangeStatusPushNotifUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : BaseOtpUseCase<ChangeStatusPushNotifPojo>(dispatcher) {

    fun getParams(
            status: Int
    ): Map<String, Any> = mapOf(
            PARAM_STATUS to status
    )

    override suspend fun getData(parameter: Map<String, Any>): ChangeStatusPushNotifPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                ChangeStatusPushNotifQuery.query,
                ChangeStatusPushNotifPojo::class.java,
                parameter
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()

    companion object {
        private const val PARAM_STATUS = "status"
    }
}