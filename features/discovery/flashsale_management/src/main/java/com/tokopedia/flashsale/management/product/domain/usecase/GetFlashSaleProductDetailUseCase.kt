package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.FlashSaleConstant.NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT
import com.tokopedia.flashsale.management.data.Result
import com.tokopedia.flashsale.management.domain.SingleGraphqlUseCaseKt
import com.tokopedia.flashsale.management.product.data.FlashSaleProduct
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject
import javax.inject.Named

class GetFlashSaleProductDetailUseCase @Inject
constructor(@Named(NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT) gqlRawString: String) {
    private val graphQLUseCase: SingleGraphqlUseCaseKt<String>
    private var gqlRawString:String? = null

    init {
        graphQLUseCase = SingleGraphqlUseCaseKt(String::class.java)
        this.gqlRawString = gqlRawString
    }

    fun getResponse(): Result<String> {
        graphQLUseCase.setRequest(GraphqlRequest(
                gqlRawString,
                FlashSaleProduct::class.java,
                null))
        return graphQLUseCase.getResponse()
    }

}
