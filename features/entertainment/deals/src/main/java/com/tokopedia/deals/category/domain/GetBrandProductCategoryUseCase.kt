package com.tokopedia.deals.category.domain

import com.tokopedia.deals.category.utils.MapperParamCategory.generateCategoryBrandPopularParams
import com.tokopedia.deals.common.domain.DealsGqlQueries
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetBrandProductCategoryUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<SearchData>() {
    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): SearchData {
        val gqlRequest = GraphqlRequest(DealsGqlQueries.getCategoryBrandProduct(),
                SearchData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.CACHE_FIRST).build())

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

    companion object{
        fun createParams(category: String, coordinates: String, location: String, page: Int): Map<String, Any> =
                generateCategoryBrandPopularParams(coordinates, location, category, page)
    }
}