package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.response.GetShopDecorStatusResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class GetShopDecorStatusUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<String>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY_SHOP_ID = "shopID"
        private const val QUERY_NAME = "ShopPageGetHomeType"
        private const val QUERY = """
            query ShopPageGetHomeType(${'$'}shopID: Int!)  {
              shopPageGetHomeType(shopID: ${'$'}shopID){
                shopHomeType
              }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(): String {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetShopDecorStatusResponse>()
        return data.shopPageGetHomeType.shopHomeType
    }

    private fun buildRequest(): GraphqlRequest {
        val params = mapOf(REQUEST_PARAM_KEY_SHOP_ID to userSession.shopId.toLong())
        return GraphqlRequest(
            ShopPageGetHomeType(),
            GetShopDecorStatusResponse::class.java,
            params
        )
    }
}