package com.tokopedia.shopdiscount.search.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.search.data.response.GetShopShowCaseResponse
import com.tokopedia.shopdiscount.search.domain.entity.CategoryFilter
import javax.inject.Inject

class GetCategoryListLiteUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<GetShopShowCaseResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY_FILTER = "filter"
        private const val REQUEST_QUERY_NAME = "categoryAllListLite"
        private const val REQUEST_QUERY = """
            query categoryAllListLite(${'$'}filter: String!){
                categoryAllListLite(filter:${'$'}filter){
                    categories {
                        id
                        name
                        children{
                            id
                            name
                            children{
                                id
                                name
                            }
                        }
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

    fun setParams(categoryFilter: CategoryFilter) {
        val params = mapOf(REQUEST_PARAM_KEY_FILTER to categoryFilter.value)
        setRequestParams(params)
    }

}