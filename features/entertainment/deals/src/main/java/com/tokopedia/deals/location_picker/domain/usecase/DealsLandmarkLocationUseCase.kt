package com.tokopedia.deals.location_picker.domain.usecase

import com.tokopedia.deals.common.domain.DealsGqlQueries
import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.location_picker.DealsLocationConstants
import com.tokopedia.deals.location_picker.model.response.LocationData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject
import javax.inject.Named

class DealsLandmarkLocationUseCase @Inject constructor(
        @Named(DealsLocationConstants.GQL_LANDMARK) private val gqlUseCase: GraphqlUseCase<LocationData>
) {
    fun getLandmarkLocation(
            onSuccess: (LocationData) -> Unit,
            onError: (Throwable) -> Unit,
            locationCoordinates: String,
            pageNo: String
    ) {
        val params = generateParams(locationCoordinates, pageNo)
        val rawQuery = DealsGqlQueries.getEventSearchQuery()
        gqlUseCase.apply {
            setTypeClass(LocationData::class.java)
            setRequestParams(params)
            setGraphqlQuery(rawQuery)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    private fun generateParams(
            locationType: String,
            pageNo: String
    ): Map<String, Any> {
        return mapOf(DealsLocationConstants.REQUEST_PARAM to
                generateSearchParams(locationType, pageNo))
    }

    private fun generateSearchParams(
            locationCoordinates: String,
            pageNo: String
    ): ArrayList<RequestParam> {
        val requestParam: ArrayList<RequestParam> = arrayListOf()
        requestParam.add(RequestParam(DealsLocationConstants.MAP_LOCATION_TYPE, DealsLocationConstants.LANDMARK))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_COORDINATES, locationCoordinates))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_SIZE, SIZE))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_PAGE_NO, pageNo))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_CATEGORY_ID, CATEGORY_ID))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_SORT_BY, PRIORITY))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_FIXED, STR_TRUE))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_DISTANCE, DISTANCE))
        return requestParam
    }

    fun cancelJobs(){
        gqlUseCase.cancelJobs()
    }

    companion object {
        const val SIZE = "10"
        const val CATEGORY_ID= "15"
        const val PRIORITY = "priority"
        const val STR_TRUE = "true"
        const val DISTANCE = "20km"
    }
}