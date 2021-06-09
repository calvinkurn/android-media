package com.tokopedia.otp.notif.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.otp.common.abstraction.BaseOtpUseCase
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifPojo
import com.tokopedia.otp.notif.domain.query.DeviceStatusPushNotifQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Ade Fulki on 22/09/20.
 */

class DeviceStatusPushNotifUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : BaseOtpUseCase<DeviceStatusPushNotifPojo>(dispatcher) {

    override suspend fun getData(parameter: Map<String, Any>): DeviceStatusPushNotifPojo = withContext(coroutineContext) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val request = GraphqlRequest(
                DeviceStatusPushNotifQuery.query,
                DeviceStatusPushNotifPojo::class.java
        )
        return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
    }.getSuccessData()
}