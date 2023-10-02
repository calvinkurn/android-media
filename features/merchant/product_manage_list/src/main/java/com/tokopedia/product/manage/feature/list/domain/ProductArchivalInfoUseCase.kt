package com.tokopedia.product.manage.feature.list.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.feature.list.data.model.ProductArchivalInfo
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject


@GqlQuery("ProductArchivalGqlQuery", ProductArchivalInfoUseCase.QUERY)
class ProductArchivalInfoUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductArchivalInfo>(graphqlRepository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PAGE_SOURCE = "pageSource"
        private const val PAGE_SOURCE_MANAGE_PRODUCT = "manage_product"

        const val QUERY = """
            query productArchival(${'$'}shopID: String!, ${'$'}productID: String!, ${'$'}pageSource: String!) {
              ProductarchivalGetProductArchiveInfo(shopID: ${'$'}shopID, productID: ${'$'}productID, pageSource: ${'$'}pageSource) {
                archiveTime
                reason
                status
                sellerEduArticleURL
                helpPageURL
              }
            }
        """

        fun createRequestParams(shopId: String, productId: String): RequestParams {
            return RequestParams().apply {
                putString(PARAM_SHOP_ID, shopId)
                putString(PARAM_PRODUCT_ID, productId)
                putString(PAGE_SOURCE, PAGE_SOURCE_MANAGE_PRODUCT)
            }

        }
    }

    init {
        setGraphqlQuery(ProductArchivalGqlQuery())
        setTypeClass(ProductArchivalInfo::class.java)
    }

    suspend fun execute(requestParams: RequestParams): ProductArchivalInfo {
        setRequestParams(requestParams.parameters)
        return executeOnBackground()
    }
}
