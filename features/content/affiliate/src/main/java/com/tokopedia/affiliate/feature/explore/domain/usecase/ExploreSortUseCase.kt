package com.tokopedia.affiliate.feature.explore.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreSort
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

/**
 * @author by milhamj on 15/03/19.
 */
class ExploreSortUseCase(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<List<SortViewModel>>() {
    override fun createObservable(requestParams: RequestParams?): Observable<List<SortViewModel>> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_af_explore_sort)
        val request = GraphqlRequest(query, ExploreSort::class.java)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map { graphqlResponse ->
            val exploreSort: ExploreSort = graphqlResponse.getData(ExploreSort::class.java)
            val itemList = ArrayList<SortViewModel>()
            exploreSort.sorts.forEach {
                itemList.add(SortViewModel(
                        it.key,
                        it.isAsc,
                        it.text,
                        false
                ))
            }
            itemList
        }
    }
}