package com.tokopedia.content.product.preview.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.preview.data.GetMiniProductInfoResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 27/11/23
 */
class ProductMiniInfoUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<Map<String, Any>, GetMiniProductInfoResponse>(dispatchers.io) {

    override suspend fun execute(params: Map<String, Any>): GetMiniProductInfoResponse {
        return repo.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = QUERY

    fun convertToMap(param: Param) : Map<String, Any> {
        return mapOf(
            PRODUCT_ID_PARAM to param.productId,
        )
    }

    data class Param (
        val productId: String,
    )

    companion object {
        private const val PRODUCT_ID_PARAM = "productID"
        private const val QUERY = """
            query getMiniInfo(${'$'}productID: String) {
                productrevGetMiniProductInfo(productID: ${'$'}productID) {
                    product {
                      id
                      name
                      thumbnailURL
                      price
                      status
                      stock
                      priceFmt
                    }
                    campaign {
                      isActive
                      discountPercentage
                      discountedPrice
                    }
                    shop {
                      id
                      name
                      badgeURL
                      isTokoNow
                    }
                    totalSold
                    totalSoldFmt
                    totalDiscussion
                    hasVariant
                    hasCashback
                    buttonState
                    categoryTree {
                      id
                      name
                      title
                      breadcrumbURL
                    }
                  }
            }
        """

    }

}
