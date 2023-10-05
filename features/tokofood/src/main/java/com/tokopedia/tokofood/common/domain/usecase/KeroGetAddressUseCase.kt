package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.common.domain.param.KeroAddressParamData
import com.tokopedia.tokofood.common.domain.response.KeroGetAddressResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.query.KeroGetAddressQuery
import javax.inject.Inject

open class KeroGetAddressUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<KeroGetAddressResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(KeroGetAddressQuery)
        setTypeClass(KeroGetAddressResponse::class.java)
    }

    suspend fun execute(addressId: String): KeroAddressParamData? {
        setRequestParams(KeroGetAddressQuery.createRequestParams(addressId))
        return executeOnBackground().keroGetAddress.data.find {
            it.addressId.toString() == addressId
        }
    }

}
