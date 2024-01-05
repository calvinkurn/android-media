package com.tokopedia.deals.ui.home.domain

import com.tokopedia.deals.common.data.DealsNearestLocationParam
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

class GetEventHomeLayoutUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<com.tokopedia.deals.ui.home.data.DealsEventHome.Response>() {

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): com.tokopedia.deals.ui.home.data.DealsEventHome.Response {
        val gqlRequest = GraphqlRequest(
            com.tokopedia.deals.ui.home.domain.DealsGqlHomeQuery.getEventHomeQuery(),
                com.tokopedia.deals.ui.home.data.DealsEventHome.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(com.tokopedia.deals.ui.home.data.DealsEventHome.Response::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(com.tokopedia.deals.ui.home.data.DealsEventHome.Response::class.java)
        }
    }

    fun useParams(params: Map<String, Any>) {
        this.params = params
    }

    companion object {
        private const val PARAM_CATEGORY = "category"
        private const val PARAM_CATEGORY_DEALS = "deal"
        private const val PARAM_ADDITIONAL_PARAMS = "additionalParams"

        fun createParams(coordinates: String, locationType: String): Map<String, Any> =
                mapOf(PARAM_CATEGORY to PARAM_CATEGORY_DEALS,
                        PARAM_ADDITIONAL_PARAMS to arrayListOf(
                            com.tokopedia.deals.ui.home.data.DealsGetHomeLayoutParam(
                                DealsNearestLocationParam.PARAM_LOCATION_TYPE,
                                locationType
                            ),
                            com.tokopedia.deals.ui.home.data.DealsGetHomeLayoutParam(
                                DealsNearestLocationParam.PARAM_COORDINATES,
                                coordinates
                            )
                ))
    }
}
