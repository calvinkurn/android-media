package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.product.data.FlashSaleMutationSubmitResponseGQL
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

class SubmitProductUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_SUBMIT_PRODUCT) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository) : GraphqlUseCase<FlashSaleMutationSubmitResponseGQL>(graphqlRepository) {

    init {
        setTypeClass(FlashSaleMutationSubmitResponseGQL::class.java)
        setGraphqlQuery(gqlRawString)
    }

    fun setParams(campaignId: Int, shopId: Int, productId: Int = 0) {
        val map = mutableMapOf<String, Any?>(
                FlashSaleConstant.PARAM_CAMP_ID to campaignId,
                FlashSaleConstant.PARAM_SHOP_ID to shopId)
        if (productId > 0) {
            map.put(FlashSaleConstant.PARAM_PRODUCT_ID, productId)
        }
        setRequestParams(map)
    }

}
