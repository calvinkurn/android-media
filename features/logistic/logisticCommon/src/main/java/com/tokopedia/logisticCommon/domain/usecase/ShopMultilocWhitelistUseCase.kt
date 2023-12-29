package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.query.ShopLocationQuery
import com.tokopedia.logisticCommon.data.request.shoplocation.KeroGetRolloutEligibilityParam
import com.tokopedia.logisticCommon.data.response.shoplocation.KeroGetRolloutEligibilityResponse
import javax.inject.Inject

class ShopMultilocWhitelistUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Long, KeroGetRolloutEligibilityResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return ShopLocationQuery.keroGetRolloutEligibility
    }

    override suspend fun execute(params: Long): KeroGetRolloutEligibilityResponse {
        val param = KeroGetRolloutEligibilityParam(shopId = params)
        return repository.request(graphqlQuery(), param)
    }
}
