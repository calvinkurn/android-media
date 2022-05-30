package com.tokopedia.tokofood.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.purchase.purchasepage.domain.model.response.KeroEditAddressResponse
import com.tokopedia.tokofood.purchase.purchasepage.domain.query.KeroEditAddressQuery
import kotlinx.coroutines.delay
import javax.inject.Inject

class KeroEditAddressUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val keroGetAddressUseCase: KeroGetAddressUseCase
) : GraphqlUseCase<KeroEditAddressResponse>(graphqlRepository) {

    private val isDebug = true

    init {
        setGraphqlQuery(KeroEditAddressQuery)
        setTypeClass(KeroEditAddressResponse::class.java)
    }

    suspend fun execute(
        addressId: String,
        latitude: String,
        longitude: String
    ): Boolean {
        if (isDebug) {
            delay(1000)
            return true
        } else {
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

}