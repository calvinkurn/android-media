package com.tokopedia.digital.home.domain

import com.tokopedia.digital.home.model.DigitalHomePageSearchAutoComplete
import com.tokopedia.digital.home.model.DigitalHomePageSearchNewModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper.mapSearchAutoCompletetoSearch
import com.tokopedia.digital.home.util.QueryDigitalHomeSearchAutoComplete
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy

class SearchAutoCompleteHomePageUseCase(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DigitalHomePageSearchAutoComplete>(graphqlRepository) {

    suspend fun searchAutoCompleteList(
        mapParams: Map<String, Any>,
        searchQuery: String
    ): DigitalHomePageSearchNewModel {
        setGraphqlQuery(QueryDigitalHomeSearchAutoComplete())
        setTypeClass(DigitalHomePageSearchAutoComplete::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setRequestParams(mapParams)

        val searchAutoComplete = executeOnBackground()
        val searchCategoryData = mapSearchAutoCompletetoSearch(searchAutoComplete, searchQuery)
        return DigitalHomePageSearchNewModel(
            true,
            searchAutoComplete.digiPersoSearchSuggestion.data.tracking,
            searchQuery,
            searchCategoryData
        )
    }

}