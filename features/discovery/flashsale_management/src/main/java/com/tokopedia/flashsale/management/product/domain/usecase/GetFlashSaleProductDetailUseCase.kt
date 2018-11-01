package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.product.data.FlashSaleProduct
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

//TODO to be deleted, detail is in list already
class GetFlashSaleProductDetailUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_PRODUCT_LIST) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository): GraphqlUseCase<FlashSaleProduct>(graphqlRepository) {

    init {
        setTypeClass(FlashSaleProduct::class.java)
        setGraphqlQuery(gqlRawString)
    }

    fun setParams(campaignId:String, offset:Int, rows:Int, query:String) {
        setRequestParams(mapOf(FlashSaleConstant.PARAM_CAMP_ID to campaignId,
                FlashSaleConstant.PARAM_OFFSET to offset,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_QUERY to query))
    }

}

