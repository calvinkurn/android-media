package com.tokopedia.common_epharmacy.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_epharmacy.EPHARMACY_PPG_SOURCE_PAP
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
        source: String? = EPHARMACY_PPG_SOURCE_PAP
    ) {
        try {
            this.setParams()
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

    private fun getRequestParams(source: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_SOURCE] = source
        return requestMap
    }

    companion object {
        const val PARAM_SOURCE = "source"
    }

    private fun setParams() {
        this.setTypeClass(EPharmacyPrepareProductsGroupResponse::class.java)
        this.setGraphqlQuery(GetEPharmacyPrepareProductsGroupQuery)
        // this.setRequestParams(getRequestParams(source))
    }
}
