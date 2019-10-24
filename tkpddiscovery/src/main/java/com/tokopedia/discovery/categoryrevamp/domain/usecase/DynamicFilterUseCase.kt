package com.tokopedia.discovery.categoryrevamp.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.filter.FilterResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject


class DynamicFilterUseCase @Inject constructor(private val context: Context) : UseCase<DynamicFilterModel>() {
    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFilterModel> {

        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_nav_dynamic_attribute), FilterResponse::class.java, requestParams?.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(FilterResponse::class.java) as FilterResponse).dynamicAttribute
        }

    }
}