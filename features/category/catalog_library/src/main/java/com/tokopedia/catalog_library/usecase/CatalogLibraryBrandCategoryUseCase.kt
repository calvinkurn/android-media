package com.tokopedia.catalog_library.usecase

import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.model.raw.gql.GQL_CATALOG_LIBRARY_BRAND_CATEGORY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GqlCatalogLibraryBrandCategory", GQL_CATALOG_LIBRARY_BRAND_CATEGORY)
class CatalogLibraryBrandCategoryUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CatalogLibraryResponse>(graphqlRepository) {

    fun getBrandCategories(
        onSuccess: (CatalogLibraryResponse) -> Unit,
        onError: (Throwable) -> Unit,
        brandId : String
    ) {
        try {
            this.setTypeClass(CatalogLibraryResponse::class.java)
            this.setRequestParams(getRequestParams(brandId))
            this.setGraphqlQuery(GqlCatalogLibraryBrandCategory())
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

    private fun getRequestParams(brandId: String?): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_BRAND_ID] = brandId
        return requestMap
    }

    companion object {
        const val PARAM_BRAND_ID = "brand_id"
    }
}
