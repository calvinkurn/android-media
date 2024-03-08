package com.tokopedia.deals.ui.search.domain.usecase

import com.tokopedia.deals.common.model.request.RequestParam
import com.tokopedia.deals.ui.search.domain.DealsSearchGqlQueries
import com.tokopedia.deals.ui.search.model.response.InitialLoadData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class DealsSearchInitialLoadUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<InitialLoadData>
) {
    fun getDealsInitialLoadResult(
        onSuccess: (InitialLoadData) -> Unit,
        onError: (Throwable) -> Unit,
        locationCoordinates: String,
        locationType: String,
        childCategoryIds: String?
    ) {
        val params = generateParams(locationCoordinates, locationType, childCategoryIds)
        val rawQuery = DealsSearchGqlQueries.getSearchInitialLoadQuery()

        gqlUseCase.apply {
            setTypeClass(InitialLoadData::class.java)
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
            locationCoordinates: String,
            locationType: String,
            childCategoryIds: String?): Map<String, Any> {
        return mapOf(
            com.tokopedia.deals.ui.search.DealsSearchConstants.SEARCH_PARAM to
                generateSearchParams(locationCoordinates, locationType, childCategoryIds),
                com.tokopedia.deals.ui.search.DealsSearchConstants.LAST_SEEN_PARAM to DEALS)
    }

    private fun generateSearchParams(
            locationCoordinates: String,
            locationType: String,
            childCategoryIds: String?): ArrayList<RequestParam> {
        val requestParams: ArrayList<RequestParam> = arrayListOf()
        requestParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_CATEGORY, DEFAULT_CATEGORY))
        requestParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_TREE, DEFAULT_TREE))
        if (childCategoryIds != null) {
            requestParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_CHILD_CATEGORY_IDS, childCategoryIds))
        }
        requestParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_COORDINATES, locationCoordinates))
        requestParams.add(RequestParam(com.tokopedia.deals.ui.search.DealsSearchConstants.MAP_LOCATION_TYPE, locationType))
        return requestParams
    }

    fun cancelJobs() {
        gqlUseCase.cancelJobs()
    }

    companion object {
        const val DEFAULT_CATEGORY = "deal"
        const val DEFAULT_TREE = "product"
        const val DEALS = "DEALS"
    }
}
