package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub

class CheckoutGeneralTokoFoodUseCaseStub(
    private val graphqlRepository: GraphqlRepositoryStub
): CheckoutGeneralTokoFoodUseCase(graphqlRepository) {

    var responseStub = CheckoutGeneralTokoFoodResponse()
        set(value) {
            graphqlRepository.createMapResult(CheckoutGeneralTokoFoodResponse::class.java, value)
            field = value
        }

    override suspend fun execute(params: CartGeneralCartListData): CheckoutGeneralTokoFoodResponse {
        return executeOnBackground()
    }

}
