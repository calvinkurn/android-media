package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.FlashSaleConstant.SOURCE_SELLERDASHBOARD
import com.tokopedia.flashsale.management.product.data.FlashSalePostProductGQL
import com.tokopedia.flashsale.management.product.data.FlashSaleSubmissionProductGQL
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

class GetPostFlashSaleProductUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_POST_PRODUCT_LIST) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository) : GraphqlUseCase<FlashSalePostProductGQL>(graphqlRepository) {

    init {
        setTypeClass(FlashSalePostProductGQL::class.java)
        setGraphqlQuery(gqlRawString)
    }

    fun setParams(campaignId: Int, start: Int, rows: Int, query: String, shopId: String) {
        val map = mutableMapOf<String, Any?>(FlashSaleConstant.PARAM_CID to campaignId,
                FlashSaleConstant.PARAM_SOURCE to SOURCE_SELLERDASHBOARD,
                FlashSaleConstant.PARAM_START to start,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_SHOP_ID to shopId)
        if (query.isNotEmpty()) {
            map.put(FlashSaleConstant.PARAM_Q, query)
        }
        setRequestParams(map)
    }

}
