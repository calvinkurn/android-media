package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.product.data.FlashSaleMutationReserveResponseGQL
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

class ReserveProductUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_RESERVE_PRODUCT) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository) : GraphqlUseCase<FlashSaleMutationReserveResponseGQL>(graphqlRepository) {

    init {
        setTypeClass(FlashSaleMutationReserveResponseGQL::class.java)
        setGraphqlQuery(gqlRawString)
    }

    fun setParams(campaignId: Int, criteriaId: Int, productId: Int,
                  discountedPrice: Int,cashback: Int,customStock: Int, shopId: Int) {
        val map = mutableMapOf<String, Any?>(
                FlashSaleConstant.PARAM_CAMPAIGN_ID to campaignId,
                FlashSaleConstant.PARAM_CRITERIA_ID to criteriaId,
                FlashSaleConstant.PARAM_PRODUCT_ID to productId,
                FlashSaleConstant.PARAM_DISCOUNTED_PRICE to discountedPrice,
                FlashSaleConstant.PARAM_CASHBACK to cashback,
                FlashSaleConstant.PARAM_CUSTOM_STOCK to customStock,
                FlashSaleConstant.PARAM_SHOP_ID to shopId)
        setRequestParams(map)
    }

}
