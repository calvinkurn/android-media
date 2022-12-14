package com.tokopedia.catalog_library.usecase

import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.raw.gql.GQL_CATALOG_LIST
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GQL_CATALOG_LIST", GQL_CATALOG_LIST)
class CatalogListUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CatalogListResponse>(graphqlRepository) {

    fun getCatalogListData(
        onSuccess: (String, CatalogListResponse) -> Unit,
        onError: (Throwable) -> Unit,
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ) {
        try {
            this.setTypeClass(CatalogListResponse::class.java)
            this.setRequestParams(getRequestParams(categoryIdentifier, sortType, rows))
            this.setGraphqlQuery(GQL_CATALOG_LIST)
            this.execute(
                { result ->
                    onSuccess(categoryIdentifier,result)
                }, { error ->
                    onError(error)
                })
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[CATEGORY_IDENTIFIER] = categoryIdentifier
        requestMap[SORT_TYPE] = sortType
        requestMap[ROWS] = rows
        return requestMap
    }

    companion object {
        const val CATEGORY_IDENTIFIER = "category_identifier"
        const val SORT_TYPE = "sortType"
        const val ROWS = "rows"
    }
}
