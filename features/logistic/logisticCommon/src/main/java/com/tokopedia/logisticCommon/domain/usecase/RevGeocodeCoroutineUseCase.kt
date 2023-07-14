package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticCommon.domain.param.ReverseGeocodeParam
import javax.inject.Inject

class RevGeocodeCoroutineUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<ReverseGeocodeParam, KeroMapsAutofill>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return KeroLogisticQuery.keroMapsAutofill
    }

    override suspend fun execute(params: ReverseGeocodeParam): KeroMapsAutofill {
        return repository.request(graphqlQuery(), params)
    }
}
