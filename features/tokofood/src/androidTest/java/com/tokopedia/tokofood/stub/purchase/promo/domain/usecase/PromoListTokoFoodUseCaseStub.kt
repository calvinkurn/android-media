package com.tokopedia.tokofood.stub.purchase.promo.domain.usecase

import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.CartGeneralPromoListDataData
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodResponse
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.PromoListTokoFoodUseCase
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import javax.inject.Inject

class PromoListTokoFoodUseCaseStub @Inject constructor(
    private val graphQlRepository: GraphqlRepositoryStub,
    chooseAddressRequestHelper: TokoFoodChosenAddressRequestHelper
): PromoListTokoFoodUseCase(graphQlRepository, chooseAddressRequestHelper) {

    var responseStub = PromoListTokoFoodResponse()
        set(value) {
            graphQlRepository.createMapResult(PromoListTokoFoodResponse::class.java, value)
            field = value
        }

    override suspend fun execute(
        source: String,
        merchantId: String,
        cartList: List<String>
    ): CartGeneralPromoListDataData {
        return executeOnBackground().cartGeneralPromoList.data.data
    }

}
