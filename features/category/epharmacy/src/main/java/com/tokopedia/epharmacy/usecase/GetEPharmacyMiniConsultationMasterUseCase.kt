package com.tokopedia.epharmacy.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.epharmacy.network.gql.GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY
import com.tokopedia.epharmacy.network.params.GetMiniConsultationBottomSheetParams
import com.tokopedia.epharmacy.network.response.GetEpharmacyMiniConsultationStaticData
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetEPharmacyMiniConsultationMasterUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetMiniConsultationBottomSheetParams, GetEpharmacyMiniConsultationStaticData>(dispatchers.io) {

    override suspend fun execute(params: GetMiniConsultationBottomSheetParams): GetEpharmacyMiniConsultationStaticData {
        return repository.request(GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY, createRequestParams(params))
    }

    override fun graphqlQuery(): String = GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY

    private fun createRequestParams(params: GetMiniConsultationBottomSheetParams): Map<String, Any> {
        return mapOf(
            PARAM_DATA_TYPE to params.dataType,
            PARAM_INPUT to params.params
        )
    }

    companion object {
        const val PARAM_INPUT = "params"
        const val PARAM_DATA_TYPE = "data_type"
    }
}
