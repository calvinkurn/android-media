package com.tokopedia.content.product.preview.data.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.preview.data.response.GetMiniProductInfoResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 27/11/23
 */
@GqlQuery(ProductMiniInfoUseCase.QUERY_NAME, ProductMiniInfoUseCase.QUERY)
class ProductMiniInfoUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ProductMiniInfoUseCase.Param, GetMiniProductInfoResponse>(dispatchers.io) {

    private val query: GqlQueryInterface = ProductMiniInfoUseCaseQuery()

    override suspend fun execute(params: Param): GetMiniProductInfoResponse {
        return repo.request(query, params)
    }

    override fun graphqlQuery(): String = query.getQuery()

    data class Param(
        @SerializedName("productID")
        val productId: String
    ) : GqlParam

    companion object {
        const val QUERY_NAME = "ProductMiniInfoUseCaseQuery"
        const val QUERY = """
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
                    }
                  }
            }
        """
    }
}
