package com.tokopedia.discovery.categoryrevamp.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.CatalogListResponse
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.SearchCatalog
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class CatalogUseCase @Inject constructor(private val context: Context,
                                         private val graphqlUseCase: GraphqlUseCase) : UseCase<SearchCatalog>() {
    override fun createObservable(requestParams: RequestParams?): Observable<SearchCatalog> {


        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_nav_search_catalog), CatalogListResponse::class.java, requestParams?.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(CatalogListResponse::class.java) as CatalogListResponse).searchCatalog
        }
    }
}