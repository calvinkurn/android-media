package com.tokopedia.common_epharmacy.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_epharmacy.network.gql.GetEPharmacyPrepareProductsGroupQuery
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class EPharmacyPrepareProductsGroupUseCase @Inject constructor(@ApplicationContext graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<EPharmacyPrepareProductsGroupResponse>(graphqlRepository) {

    fun getEPharmacyPrepareProductsGroup(source: String, onSuccess: (String, EPharmacyPrepareProductsGroupResponse) -> Unit,
                                   onError: (Throwable) -> Unit) {
        try {
            this.setParams()
            this.execute(
                { result ->
                    onSuccess(source, result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    fun setParams() {
        this.setTypeClass(EPharmacyPrepareProductsGroupResponse::class.java)
        this.setGraphqlQuery(GetEPharmacyPrepareProductsGroupQuery)
    }
}
