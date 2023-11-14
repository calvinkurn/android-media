package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.query.KeroEditAddressQuery
import javax.inject.Inject

open class KeroEditAddressUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val keroGetAddressUseCase: KeroGetAddressUseCase
) : GraphqlUseCase<KeroEditAddressResponse.Data>(
    graphqlRepository
) {

    init {
        setGraphqlQuery(KeroEditAddressQuery)
        setTypeClass(KeroEditAddressResponse.Data::class.java)
    }

    suspend fun execute(
        addressId: String,
        latitude: String,
        longitude: String
    ): KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse {
        val addressParam = keroGetAddressUseCase.execute(addressId)?.copy(
            latitude = latitude,
            longitude = longitude,
            secondAddress = "$latitude,$longitude"
        )
        return if (addressParam == null) {
            KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse(isSuccess = 0)
        } else {
            setRequestParams(KeroEditAddressQuery.createRequestParams(addressParam))
            executeOnBackground().keroEditAddress.data
        }
    }
}
