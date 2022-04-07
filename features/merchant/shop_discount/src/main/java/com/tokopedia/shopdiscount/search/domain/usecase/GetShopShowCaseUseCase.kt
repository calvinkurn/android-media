package com.tokopedia.shopdiscount.search.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.manage.data.response.GetShopShowCaseResponse
import javax.inject.Inject

class GetShopShowCaseUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<GetShopShowCaseResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY_SHOP_ID = "shopId"
        private const val REQUEST_PARAM_KEY_HIDE_NO_COUNT = "hideNoCount"
        private const val REQUEST_PARAM_KEY_HIDE_SHOWCASE_GROUP = "hideShowcaseGroup"
        private const val REQUEST_PARAM_KEY_IS_OWNER = "isOwner"

        private const val REQUEST_QUERY_NAME = "shopShowcasesByShopID"
        private const val REQUEST_QUERY = """
            query shopShowcasesByShopID(${'$'}shopId: String, ${'$'}hideNoCount: Boolean, ${'$'}hideShowcaseGroup: Boolean, ${'$'}isOwner:Boolean) {
                  shopShowcasesByShopID(shopId: ${'$'}shopId, hideNoCount: ${'$'}hideNoCount, hideShowcaseGroup: ${'$'}hideShowcaseGroup, isOwner:${'$'}isOwner) {
                        result {
                            id
                            name
                            count
                            type
                            highlighted
                            alias
                            uri
                            useAce
                            badge
                            aceDefaultSort
                        }
                        error {
                            message
                        }
                    }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(REQUEST_QUERY_NAME, REQUEST_QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(ShopShowcasesByShopID())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetShopShowCaseResponse::class.java)
    }

    fun setParams(
        shopId: String,
        hideNoCount: Boolean = true,
        hideShowcaseGroup: Boolean = true,
        isOwner: Boolean = true
    ) {
        val params = mapOf(
            REQUEST_PARAM_KEY_SHOP_ID to shopId,
            REQUEST_PARAM_KEY_HIDE_NO_COUNT to hideNoCount,
            REQUEST_PARAM_KEY_HIDE_SHOWCASE_GROUP to hideShowcaseGroup,
            REQUEST_PARAM_KEY_IS_OWNER to isOwner
        )
        setRequestParams(params)
    }

}