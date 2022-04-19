package com.tokopedia.tokofood.purchase.purchasepage.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.KeroEditAddressResponse
import com.tokopedia.tokofood.purchase.purchasepage.domain.query.KeroEditAddressQuery
import kotlinx.coroutines.delay
import javax.inject.Inject

class KeroEditAddressUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<KeroEditAddressResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(KeroEditAddressQuery)
        setTypeClass(KeroEditAddressResponse::class.java)
    }

    suspend fun execute(): Boolean {
        // TODO: Pass actual params and run actual query
        setRequestParams(KeroEditAddressQuery.createRequestParams())
//        return executeOnBackground().keroEditAddress.data.isEditSuccess()
        delay(1000)
        return true
    }

}