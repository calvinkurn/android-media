package com.tokopedia.categorylevels.domain.usecase

import com.tokopedia.common_category.data.catalogModel.CatalogListResponse
import com.tokopedia.common_category.data.catalogModel.SearchCatalog
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.categorylevels.data.raw.GQL_NAV_SEARCH_CATALOG
import rx.Observable
import javax.inject.Inject

class CatalogUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<SearchCatalog>() {
    override fun createObservable(requestParams: RequestParams?): Observable<SearchCatalog> {


        val graphqlRequest = GraphqlRequest(GQL_NAV_SEARCH_CATALOG, CatalogListResponse::class.java, requestParams?.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(CatalogListResponse::class.java) as CatalogListResponse).searchCatalog
        }
    }
}