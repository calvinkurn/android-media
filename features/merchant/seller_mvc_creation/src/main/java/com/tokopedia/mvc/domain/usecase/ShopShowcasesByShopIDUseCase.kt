package com.tokopedia.mvc.domain.usecase


import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.ShopShowcasesByShopIDMapper
import com.tokopedia.mvc.data.response.ShopShowcasesByShopIDResponse
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopShowcasesByShopIDUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: ShopShowcasesByShopIDMapper,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<List<ShopShowcase>>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_SHOP_ID = "shopId"
        private const val REQUEST_PARAM_HIDE_NO_COUNT = "hideNoCount"
        private const val REQUEST_PARAM_HIDE_SHOWCASE_GROUP = "hideShowcaseGroup"
        private const val REQUEST_PARAM_IS_OWNER = "isOwner"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "shopShowcasesByShopID"
        private val QUERY = """
        query $OPERATION_NAME(${'$'}shopId: String!) {
             $OPERATION_NAME(shopId: ${'$'}shopId) {
                    result {
                        id
                        name
                        alias
                        type
                    }
                    error {
                        message
                    }
             }
       }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(): List<ShopShowcase> {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<ShopShowcasesByShopIDResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(): GraphqlRequest {
        val shopId = userSession.shopId
        val params = mapOf(
            REQUEST_PARAM_SHOP_ID to shopId,
            REQUEST_PARAM_HIDE_NO_COUNT to true,
            REQUEST_PARAM_HIDE_SHOWCASE_GROUP to true,
            REQUEST_PARAM_IS_OWNER to true
        )

        return GraphqlRequest(
            query,
            ShopShowcasesByShopIDResponse::class.java,
            params
        )
    }
}

