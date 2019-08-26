package com.tokopedia.discovery.categoryrevamp.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SubCategoryUseCase @Inject constructor(private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<ArrayList<SubCategoryItem?>?>() {

    override fun createObservable(requestParams: RequestParams?): Observable<ArrayList<SubCategoryItem?>?> {

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_nav_category_detail), SubCategoryResponse::class.java, requestParams?.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val list = ((it.getData(SubCategoryResponse::class.java) as SubCategoryResponse).categoryDetailQuery?.data?.child) as ArrayList
            if (list.isNotEmpty()) {
                val item = SubCategoryItem()
                item.name = "Semua"
                item.is_default = true
                list.add(0, item)
            }
            list
        }

    }
}