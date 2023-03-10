package com.tokopedia.catalog_library.usecase

import com.tokopedia.catalog_library.model.raw.CatalogBrandsPopularResponse
import com.tokopedia.catalog_library.model.raw.gql.GQL_CATALOG_BRAND_POPULAR_WITH_CATALOGS
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GqlCatalogGetPopularBrandsWithCatalogs", GQL_CATALOG_BRAND_POPULAR_WITH_CATALOGS)
class CatalogBrandsPopularWithCatalogsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CatalogBrandsPopularResponse>(graphqlRepository) {

    fun getBrandPopularWithCatalogs(
        onSuccess: (CatalogBrandsPopularResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(CatalogBrandsPopularResponse::class.java)
            this.setGraphqlQuery(GqlCatalogGetPopularBrandsWithCatalogs())
            this.execute(
                { result ->
                    onSuccess(result)
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }
}
