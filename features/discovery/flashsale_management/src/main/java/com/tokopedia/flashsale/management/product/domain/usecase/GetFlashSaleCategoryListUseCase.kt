
package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.FlashSaleFilterProductListTypeDef
import com.tokopedia.flashsale.management.product.data.FlashSaleCategoryListGQL
import com.tokopedia.flashsale.management.product.data.FlashSaleProductGQL
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

class GetFlashSaleCategoryListUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_CATEGORY_LIST) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository): GraphqlUseCase<FlashSaleCategoryListGQL>(graphqlRepository) {

    init {
        setTypeClass(FlashSaleCategoryListGQL::class.java)
        setGraphqlQuery(gqlRawString)
    }

    fun setParams(campaignSlug:Int, shopId:Int) {
        val map = mutableMapOf<String, Any?>(
                FlashSaleConstant.PARAM_SLUG to campaignSlug,
                FlashSaleConstant.PARAM_SHOP_ID to shopId)
        setRequestParams(map)
    }

}
