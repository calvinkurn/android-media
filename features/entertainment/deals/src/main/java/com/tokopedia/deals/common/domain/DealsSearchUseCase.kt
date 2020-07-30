package com.tokopedia.deals.common.domain

import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.utils.MapperParamSearch
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class DealsSearchUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<SearchData>
) {
    fun getDealsSearchResult(
            onSuccess: (SearchData) -> Unit,
            onError: (Throwable) -> Unit,
            searchQuery: String,
            locationCoordinates: String,
            locationType: String,
            childCategoryIds: String?,
            page: String,
            rawQuery: String,
            tree: String
    ) {
        val params = MapperParamSearch.generateParams(searchQuery, locationCoordinates, locationType, childCategoryIds, page, tree)
        gqlUseCase.apply {
            setTypeClass(SearchData::class.java)
            setRequestParams(params)
            setGraphqlQuery(rawQuery)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    fun cancelJobs() {
        gqlUseCase.cancelJobs()
    }
}