package com.tokopedia.catalog_library.usecase

import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.raw.gql.GQL_CATALOG_LIST
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("GqlCatalogList", GQL_CATALOG_LIST)
class CatalogProductsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<CatalogListResponse>(graphqlRepository) {

    fun getCatalogProductsData(
        onSuccess: (CatalogListResponse, Int) -> Unit,
        onError: (Throwable) -> Unit,
        categoryId: String,
        sortType: Int,
        rows: Int,
        page: Int = 1,
        brandId: String = "",
    ) {
        try {
            this.setTypeClass(CatalogListResponse::class.java)
            this.setRequestParams(
                getRequestParams(
                    categoryId,
                    sortType,
                    rows,
                    page,
                    brandId
                )
            )
            this.setGraphqlQuery(GqlCatalogList())
            this.execute(
                { result ->
                    onSuccess(result, page)
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(
        categoryId: String,
        sortType: Int,
        rows: Int,
        page: Int,
        brandId : String
    ): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[CATEGORY_ID] = categoryId
        requestMap[SORT_TYPE] = sortType.toString()
        requestMap[ROWS] = rows.toString()
        requestMap[PAGE] = page.toString()
        if(brandId.isNotBlank()){
            requestMap[BRAND_ID] = brandId
        }
        return requestMap
    }

    companion object {
        const val CATEGORY_ID = "category_id"
        const val BRAND_ID = "brand_id"
        const val SORT_TYPE = "sortType"
        const val ROWS = "rows"
        const val PAGE = "page"
    }
}
