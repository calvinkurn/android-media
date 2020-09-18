package com.tokopedia.affiliate.feature.explore.domain.usecase

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
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<List<SortViewModel>>() {

    private val query = """
        query {
          topadsGetExploreSort {
              sort {
                  key
                  asc
                  text
                }
           }
        }
    """

    override fun createObservable(requestParams: RequestParams?): Observable<List<SortViewModel>> {
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