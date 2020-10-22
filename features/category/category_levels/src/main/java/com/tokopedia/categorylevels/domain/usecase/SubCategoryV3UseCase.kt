package com.tokopedia.categorylevels.domain.usecase

import com.tokopedia.common_category.data.raw.GQL_NAV_CATEGORY_DETAIL_V3
import com.tokopedia.common_category.model.bannedCategory.BannedCategoryResponse
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SubCategoryV3UseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<Data?>() {
    override fun createObservable(requestParams: RequestParams?): Observable<Data?> {

        val graphqlRequest = GraphqlRequest(GQL_NAV_CATEGORY_DETAIL_V3, BannedCategoryResponse::class.java, requestParams?.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            ((it.getData(BannedCategoryResponse::class.java) as BannedCategoryResponse).categoryDetailQuery?.data) as Data
        }

    }

}