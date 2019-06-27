package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.product.data.FlashSaleMutationDeReserveResponseGQL
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

class DereserveProductUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_DERESERVE_PRODUCT) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository) : GraphqlUseCase<FlashSaleMutationDeReserveResponseGQL>(graphqlRepository) {

    init {
        setTypeClass(FlashSaleMutationDeReserveResponseGQL::class.java)
        setGraphqlQuery(gqlRawString)
    }

    fun setParams(campaignId: Int, productId: Int, shopId: Int) {
        val map = mutableMapOf<String, Any?>(
                FlashSaleConstant.PARAM_CAMPAIGN_ID to campaignId,
                FlashSaleConstant.PARAM_PRODUCT_ID to productId,
                FlashSaleConstant.PARAM_SHOP_ID to shopId)
        setRequestParams(map)
    }

}
