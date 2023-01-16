package com.tokopedia.catalog_library.usecase

import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.model.raw.gql.GQL_CATALOG_LIBRARY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GQL_CATALOG_LIBRARY", GQL_CATALOG_LIBRARY)
class CatalogLibraryUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CatalogLibraryResponse>(graphqlRepository) {

    fun getLibraryData(
        onSuccess: (CatalogLibraryResponse) -> Unit,
        onError: (Throwable) -> Unit,
        sortOrder: String?
    ) {
        try {
            this.setTypeClass(CatalogLibraryResponse::class.java)
            this.setRequestParams(getRequestParams(sortOrder))
            this.setGraphqlQuery(GQL_CATALOG_LIBRARY)
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(sortOrder: String?): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_SORT_ORDER] = sortOrder
        return requestMap
    }

    companion object {
        const val PARAM_SORT_ORDER = "sortOrder"
    }
}
