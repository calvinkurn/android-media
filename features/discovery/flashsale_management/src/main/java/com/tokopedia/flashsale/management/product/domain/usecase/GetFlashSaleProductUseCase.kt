package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.FlashSaleConstant.NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT
import com.tokopedia.flashsale.management.data.Result
import com.tokopedia.flashsale.management.domain.SingleGraphqlUseCaseKt
import com.tokopedia.flashsale.management.product.data.FlashSaleProduct
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject
import javax.inject.Named

class GetFlashSaleProductUseCase @Inject
constructor(@Named(NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT) gqlRawString: String) {
    private val graphQLUseCase: SingleGraphqlUseCaseKt<FlashSaleProduct>
    private var gqlRawString:String? = null

    init {
        graphQLUseCase = SingleGraphqlUseCaseKt(FlashSaleProduct::class.java)
        this.gqlRawString = gqlRawString
    }

    fun getResponse(campaignId:String, offset:Int, rows:Int, query:String): Result<FlashSaleProduct> {
        val parameters = mapOf(FlashSaleConstant.PARAM_CAMP_ID to campaignId,
                FlashSaleConstant.PARAM_OFFSET to offset,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_QUERY to query)
        graphQLUseCase.setRequest(GraphqlRequest(
                gqlRawString,
                FlashSaleProduct::class.java,
                parameters))
        return graphQLUseCase.getResponse()
    }

}
