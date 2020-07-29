package com.tokopedia.deals.home.domain

import com.tokopedia.deals.common.data.DealsNearestLocationParam
import com.tokopedia.deals.home.data.DealsGetHomeLayoutParam
import com.tokopedia.deals.search.domain.DealsSearchGqlQueries
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 19/06/20
 */

class GetEventHomeBrandPopularUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<SearchData>() {

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): SearchData {
        val gqlRequest = GraphqlRequest(DealsSearchGqlQueries.getEventSearchQuery(),
                SearchData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(SearchData::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(SearchData::class.java)
        }
    }

    fun useParams(params: Map<String, Any>) {
        this.params = params
    }

    companion object {
        private const val PARAM_CATEGORY = "category"
        private const val PARAM_CATEGORY_DEALS = "deal"
        private const val PARAM_ADDITIONAL_PARAMS = "params"
        private const val PARAM_DEALS_TREE = "tree"
        private const val PARAM_DEALS_TREE_VALUE_BRAND = "brand"
        private const val PARAM_SIZE = "page_size"

        fun createParams(coordinates: String, locationType: String, size: String): Map<String, Any> =
                mapOf(PARAM_ADDITIONAL_PARAMS to arrayListOf(
                        DealsGetHomeLayoutParam(PARAM_CATEGORY, PARAM_CATEGORY_DEALS),
                        DealsGetHomeLayoutParam(PARAM_DEALS_TREE, PARAM_DEALS_TREE_VALUE_BRAND),
                        DealsGetHomeLayoutParam(DealsNearestLocationParam.PARAM_COORDINATES, coordinates),
                        DealsGetHomeLayoutParam(DealsNearestLocationParam.PARAM_LOCATION_TYPE, locationType),
                        DealsGetHomeLayoutParam(PARAM_SIZE, size)
                ))
    }
}