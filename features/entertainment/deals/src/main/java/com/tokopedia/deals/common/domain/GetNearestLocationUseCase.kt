package com.tokopedia.deals.common.domain

import com.tokopedia.deals.common.data.DealsNearestLocationParam
import com.tokopedia.deals.location_picker.model.response.LocationData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 15/06/20
 */

class GetNearestLocationUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<LocationData>() {

    var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): LocationData {
        val gqlRequest = GraphqlRequest(DealsGqlQueries.getEventSearchQuery(),
                LocationData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(LocationData::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(LocationData::class.java)
        }
    }

    fun useParams(params: Map<String, Any>) {
        this.params = params
    }


    companion object {
        private const val PARAM_SEARCH_PARAM = "params"

        fun createParams(coordinates: String, size: String): Map<String, Any> =
                mapOf(PARAM_SEARCH_PARAM to arrayListOf(
                        DealsNearestLocationParam(DealsNearestLocationParam.PARAM_LOCATION_TYPE, DealsNearestLocationParam.VALUE_LOCATION_TYPE_CITY),
                        DealsNearestLocationParam(DealsNearestLocationParam.PARAM_COORDINATES, coordinates),
                        DealsNearestLocationParam(DealsNearestLocationParam.PARAM_SIZE, size),
                        DealsNearestLocationParam(DealsNearestLocationParam.PARAM_CATEGORY_ID, DealsNearestLocationParam.VALUE_CATEGORY_ID_DEFAULT)
                ))

        fun createParams(locationType: String, coordinates: String, size: String, pageNum: String,
                         categoryId: String, distance: String): Map<String, Any> {
            return mapOf(PARAM_SEARCH_PARAM to arrayListOf(
                    DealsNearestLocationParam(DealsNearestLocationParam.PARAM_LOCATION_TYPE, locationType),
                    DealsNearestLocationParam(DealsNearestLocationParam.PARAM_COORDINATES, coordinates),
                    DealsNearestLocationParam(DealsNearestLocationParam.PARAM_SIZE, size),
                    DealsNearestLocationParam(DealsNearestLocationParam.PARAM_PAGE_NUM, pageNum),
                    DealsNearestLocationParam(DealsNearestLocationParam.PARAM_CATEGORY_ID, categoryId),
                    DealsNearestLocationParam(DealsNearestLocationParam.PARAM_DISTANCE, distance)
            ))
        }
    }
}
