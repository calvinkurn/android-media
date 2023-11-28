package com.tokopedia.common_epharmacy.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_epharmacy.EPHARMACY_PPG_SOURCE_CHECKOUT
import com.tokopedia.common_epharmacy.network.gql.GetEPharmacyPrepareProductsGroupQuery
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyPrepareProductsGroupUseCase @Inject constructor(@ApplicationContext graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyPrepareProductsGroupResponse>(graphqlRepository) {

    fun getEPharmacyPrepareProductsGroup(
        onSuccess: (EPharmacyPrepareProductsGroupResponse, String?) -> Unit,
        onError: (Throwable) -> Unit,
        source: String? = EPHARMACY_PPG_SOURCE_CHECKOUT,
        params: MutableMap<String, Any?>
    ) {
        try {
            this.setParams(params)
            this.execute(
                { result ->
                    onSuccess(result, source)
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    companion object {
        const val PARAM_INPUT = "input"
        const val PARAM_SOURCE = "source"
        const val PARAM_TOKO_CONSULTATION_IDS = "tokoConsultationIDs"
    }

    fun setParams(params: Map<String, Any?>) {
        this.setTypeClass(EPharmacyPrepareProductsGroupResponse::class.java)
        this.setGraphqlQuery(GetEPharmacyPrepareProductsGroupQuery)
        this.setRequestParams(mapOf(PARAM_INPUT to params))
    }
}
