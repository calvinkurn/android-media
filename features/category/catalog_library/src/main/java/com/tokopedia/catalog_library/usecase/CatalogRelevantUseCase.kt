package com.tokopedia.catalog_library.usecase

import com.tokopedia.catalog_library.model.raw.CatalogRelevantResponse
import com.tokopedia.catalog_library.model.raw.gql.GQL_CATALOG_RELEVANT
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GqlCatalogRelevant", GQL_CATALOG_RELEVANT)
class CatalogRelevantUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CatalogRelevantResponse>(graphqlRepository) {

    fun getRelevantData(
        onSuccess: (CatalogRelevantResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(CatalogRelevantResponse::class.java)
            this.setGraphqlQuery(GqlCatalogRelevant())
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                })
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}
