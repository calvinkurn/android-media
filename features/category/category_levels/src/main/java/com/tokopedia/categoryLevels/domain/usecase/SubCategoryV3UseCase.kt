package com.tokopedia.categoryLevels.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.categoryLevels.R
import com.tokopedia.common_category.model.bannedCategory.BannedCategoryResponse
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SubCategoryV3UseCase @Inject constructor(private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<Data?>() {
    override fun createObservable(requestParams: RequestParams?): Observable<Data?> {

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_nav_category_detail_v3), BannedCategoryResponse::class.java, requestParams?.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
           ((it.getData(BannedCategoryResponse::class.java) as BannedCategoryResponse).categoryDetailQuery?.data) as Data
        }

    }

}