package com.tokopedia.deals.location_picker.domain.usecase

import com.tokopedia.deals.common.domain.DealsGqlQueries
import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.location_picker.DealsLocationConstants
import com.tokopedia.deals.location_picker.model.response.LocationData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject
import javax.inject.Named

class DealsSearchLocationUseCase @Inject constructor(
        @Named(DealsLocationConstants.GQL_SEARCH_LOCATION) private val gqlUseCase: GraphqlUseCase<LocationData>
) {
    fun getSearchedLocation(
            onSuccess: (LocationData) -> Unit,
            onError: (Throwable) -> Unit,
            name: String,
            pageNo: String
    ) {
        val params = generateParams(name, pageNo)
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
            name: String,
            pageNo: String
    ): Map<String, Any> {
        return mapOf(DealsLocationConstants.REQUEST_PARAM to
                generateSearchParams(name, pageNo))
    }

    private fun generateSearchParams(
            name: String,
            pageNo: String
    ): ArrayList<RequestParam> {
        val requestParam: ArrayList<RequestParam> = arrayListOf()
        requestParam.add(RequestParam(DealsLocationConstants.MAP_CATEGORY_ID, CATEGORY_ID))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_NAME, name))
        requestParam.add(RequestParam(DealsLocationConstants.MAP_PAGE_NO, pageNo))
        return requestParam
    }

    fun cancelJobs(){
        gqlUseCase.cancelJobs()
    }

    companion object {
        const val CATEGORY_ID= "15"
    }
}