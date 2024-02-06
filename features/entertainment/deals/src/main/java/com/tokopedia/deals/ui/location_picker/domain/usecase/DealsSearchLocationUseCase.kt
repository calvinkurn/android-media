package com.tokopedia.deals.ui.location_picker.domain.usecase

import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.domain.DealsGqlQueries
import com.tokopedia.deals.ui.location_picker.model.response.LocationData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject
import javax.inject.Named

class DealsSearchLocationUseCase @Inject constructor(
        @Named(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.GQL_SEARCH_LOCATION) private val gqlUseCase: GraphqlUseCase<LocationData>
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
        return mapOf(
            com.tokopedia.deals.ui.location_picker.DealsLocationConstants.REQUEST_PARAM to
                generateSearchParams(name, pageNo))
    }

    private fun generateSearchParams(
            name: String,
            pageNo: String
    ): ArrayList<RequestParam> {
        val requestParam: ArrayList<RequestParam> = arrayListOf()
        requestParam.add(RequestParam(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.MAP_CATEGORY_ID, CATEGORY_ID))
        requestParam.add(RequestParam(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.MAP_NAME, name))
        requestParam.add(RequestParam(com.tokopedia.deals.ui.location_picker.DealsLocationConstants.MAP_PAGE_NO, pageNo))
        return requestParam
    }

    fun cancelJobs(){
        gqlUseCase.cancelJobs()
    }

    companion object {
        const val CATEGORY_ID= "15"
    }
}
