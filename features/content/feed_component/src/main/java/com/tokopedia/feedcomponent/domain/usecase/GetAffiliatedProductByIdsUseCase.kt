package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.domain.SUSPEND_GRAPHQL_REPOSITORY
import com.tokopedia.feedcomponent.domain.model.commission.AffiliatedProductByProductIDs
import com.tokopedia.feedcomponent.domain.model.commission.GetAffiliatedProductByProductIDsResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-11-22
 */
class GetAffiliatedProductByIdsUseCase @Inject constructor(
        @param:Named(SUSPEND_GRAPHQL_REPOSITORY) private val graphqlRepository: GraphqlRepository
) : UseCase<AffiliatedProductByProductIDs>() {

    companion object {

        private const val PARAM_PRODUCT_IDS = "productIDs"

        fun getParam(productIDs: List<String>): RequestParams {
            return RequestParams.create().apply {
                putObject(PARAM_PRODUCT_IDS, productIDs)
            }
        }
    }

    private val params: MutableMap<String, Any> = mutableMapOf()

    private val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()

    //region query
    private val query by lazy {
        val productIDs = "\$productIDs"

        """
            query GetAffiliatedProductByIDs($productIDs: [String!]!) {
                affiliatedProductByProductIDs(productIDs: $productIDs) {
                    totalProductCommission
                    products {
                        id
                        image
                        name
                        isActive
                        totalSold
                        totalClick
                        commission
                        productCommission
                        productRating
                        createPostAppLink
                        adID
                        productID
                        reviewCount
                    }
                }
            }
        """.trimIndent()
    }
    //endregion

    override suspend fun executeOnBackground(): AffiliatedProductByProductIDs {
        val response = graphqlRepository.getReseponse(
                listOf(
                        GraphqlRequest(query, GetAffiliatedProductByProductIDsResponse::class.java, params)
                ),
                cacheStrategy
        )
        return response.getData<GetAffiliatedProductByProductIDsResponse>(GetAffiliatedProductByProductIDsResponse::class.java).affiliatedProductByProductIDs
    }

    fun setParams(params: RequestParams) {
        this.params.run {
            clear()
            putAll(params.parameters)
        }
    }
}