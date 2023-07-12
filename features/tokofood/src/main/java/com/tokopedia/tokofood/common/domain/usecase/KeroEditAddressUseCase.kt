package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.common.domain.response.KeroEditAddressResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.query.KeroEditAddressQuery
import javax.inject.Inject

open class KeroEditAddressUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val keroGetAddressUseCase: KeroGetAddressUseCase
) : GraphqlUseCase<KeroEditAddressResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(KeroEditAddressQuery)
        setTypeClass(KeroEditAddressResponse::class.java)
    }

    suspend fun execute(
        addressId: String,
        latitude: String,
        longitude: String
    ): Boolean {
        val addressParam = keroGetAddressUseCase.execute(addressId)?.copy(
            latitude = latitude,
            longitude = longitude,
            secondAddress = "$latitude,$longitude"
        )
        return if (addressParam == null) {
            false
        } else {
            setRequestParams(KeroEditAddressQuery.createRequestParams(addressParam))
            executeOnBackground().keroEditAddress.data.isEditSuccess()
        }
    }

}
