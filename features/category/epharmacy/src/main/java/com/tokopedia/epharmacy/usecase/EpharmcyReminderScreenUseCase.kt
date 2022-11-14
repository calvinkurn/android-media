package com.tokopedia.epharmacy.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.epharmacy.network.gql.GQL_FETCH_ORDER_DETAILS_QUERY
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class EpharmcyReminderScreenUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, EPharmacyDataResponse.EPharmacyOrderDetailData
    >(dispatchers.io) {

    override suspend fun execute(params: Int): EPharmacyDataResponse.EPharmacyOrderDetailData {
        return repository.request(GQL_FETCH_ORDER_DETAILS_QUERY, createRequestParams(params))
    }

    override fun graphqlQuery(): String = GQL_FETCH_ORDER_DETAILS_QUERY

    private fun createRequestParams(params: Int): Map<String, Any> {
        return mapOf<String, Any>()
    }

}
