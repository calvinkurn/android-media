package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.FlashSaleConstant.NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT
import com.tokopedia.flashsale.management.product.data.FlashSaleProduct
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class GetFlashSaleProductUseCase @Inject
constructor(@Named(NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT) private val gqlRawString: String): UseCase<FlashSaleProduct>() {

    private val params = mutableMapOf<String, Any>()

    override suspend fun executeOnBackground(): FlashSaleProduct {
        throw IOException()
    }

    fun setParams(campaignId:String, offset:Int, rows:Int, query:String) {
        params.putAll(mapOf(FlashSaleConstant.PARAM_CAMP_ID to campaignId,
                FlashSaleConstant.PARAM_OFFSET to offset,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_QUERY to query))
    }

}
