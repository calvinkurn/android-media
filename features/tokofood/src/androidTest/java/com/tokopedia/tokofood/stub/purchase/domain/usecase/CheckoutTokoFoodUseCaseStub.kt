package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.CartListTokofoodResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub

class CheckoutTokoFoodUseCaseStub(
    private val graphQlRepository: GraphqlRepositoryStub,
    chosenAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): CheckoutTokoFoodUseCase(graphQlRepository, chosenAddressRequestHelper) {

    var responseStub = CartListTokofoodResponse()
        set(value) {
            graphQlRepository.createMapResult(CartListTokofoodResponse::class.java, value)
            field = value
        }

    override suspend fun execute(source: String): CartGeneralCartListData {
        return executeOnBackground().cartGeneralCartList.data
    }

}
