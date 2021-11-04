package com.tokopedia.digital.home.old.domain

import com.tokopedia.digital.home.model.DigitalHomePageSearchAutoComplete
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper.mapSearchAutoCompletetoSearch
import com.tokopedia.digital.home.util.DigitalHomepageGqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy

class SearchAutoCompleteHomePageUseCase (graphqlRepository: GraphqlRepository): GraphqlUseCase<DigitalHomePageSearchAutoComplete>(graphqlRepository) {

    suspend fun searchAutoCompleteList(mapParams: Map<String, Any>): List<DigitalHomePageSearchCategoryModel> {
        setGraphqlQuery(DigitalHomepageGqlQuery.searchAutoComplete)
        setTypeClass(DigitalHomePageSearchAutoComplete::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setRequestParams(mapParams)

        val searchCategoryData = mapSearchAutoCompletetoSearch(executeOnBackground())
        return searchCategoryData
    }
}