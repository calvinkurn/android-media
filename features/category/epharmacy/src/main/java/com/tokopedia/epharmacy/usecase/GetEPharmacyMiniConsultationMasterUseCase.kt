package com.tokopedia.epharmacy.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.epharmacy.network.gql.GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY
import com.tokopedia.epharmacy.network.params.GetMiniConsultationBottomSheetParams
import com.tokopedia.epharmacy.network.response.EPharmacyMiniConsultationMasterResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetEPharmacyMiniConsultationMasterUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetMiniConsultationBottomSheetParams, EPharmacyMiniConsultationMasterResponse>(dispatchers.io) {

    override suspend fun execute(params: GetMiniConsultationBottomSheetParams): EPharmacyMiniConsultationMasterResponse {
        return repository.request(GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY, params)
    }

    override fun graphqlQuery(): String = GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY
}
