package com.tokopedia.hotel.search.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.hotel.search.data.model.params.SearchParam
import com.tokopedia.hotel.search.presentation.viewmodel.HotelSearchResultViewModel
import com.tokopedia.hotel.search_map.presentation.viewmodel.HotelSearchMapViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 31/08/20
 */

class SearchPropertyUseCase @Inject constructor(val graphqlRepository: GraphqlRepository) :
        GraphqlUseCase<PropertySearch.Response>(graphqlRepository) {
    suspend fun execute(rawQuery: String, searchParam: SearchParam): Result<PropertySearch> {
        return try {
            this.setTypeClass(PropertySearch.Response::class.java)
            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(mapOf(HotelSearchMapViewModel.PARAM_SEARCH_PROPERTY to searchParam))

            val data = this.executeOnBackground()
            Success(data.response)
        } catch (t: Throwable) {
            Fail(t)
        }
    }
}