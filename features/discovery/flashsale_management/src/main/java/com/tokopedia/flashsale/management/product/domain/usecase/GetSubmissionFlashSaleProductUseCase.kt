package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.FlashSaleFilterProductListTypeDef
import com.tokopedia.flashsale.management.product.data.FlashSaleSubmissionProductGQL
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

class GetSubmissionFlashSaleProductUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_SUBMISSION_PRODUCT_LIST) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository): GraphqlUseCase<FlashSaleSubmissionProductGQL>(graphqlRepository) {

    init {
        setTypeClass(FlashSaleSubmissionProductGQL::class.java)
        setGraphqlQuery(gqlRawString)
    }

    fun setParams(campaignId:Int, offset:Int, rows:Int, query:String, shopId:Int,
                  filterId:Int) {
        val map = mutableMapOf<String, Any?>(FlashSaleConstant.PARAM_CAMP_ID to campaignId,
                FlashSaleConstant.PARAM_OFFSET to offset,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_SHOP_ID to shopId)
        if (query.isNotEmpty()) {
            map.put(FlashSaleConstant.PARAM_Q, query)
        }
        if (filterId != FlashSaleFilterProductListTypeDef.TYPE_ALL.id) {
            map.put(FlashSaleConstant.PARAM_FILTER, filterId)
        }
        setRequestParams(map)
    }

}
