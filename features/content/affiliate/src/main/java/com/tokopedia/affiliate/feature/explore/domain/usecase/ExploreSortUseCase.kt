package com.tokopedia.affiliate.feature.explore.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreSortData
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 15/03/19.
 */
class ExploreSortUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<List<SortViewModel>>() {
    override fun createObservable(requestParams: RequestParams?): Observable<List<SortViewModel>> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_af_explore_sort)
        val request = GraphqlRequest(query, ExploreSortData::class.java)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map { graphqlResponse ->
            val data: ExploreSortData = graphqlResponse.getData(ExploreSortData::class.java)
            val itemList = ArrayList<SortViewModel>()
            data.exploreSort.sorts.forEach {
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