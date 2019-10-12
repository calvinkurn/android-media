package com.tokopedia.affiliate.feature.dashboard.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.dashboard.data.pojo.AffiliateProductSortQuery
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetCuratedProductSortUseCase
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetCuratedProductListUseCase
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateCuratedProductContract
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetCuratedProductListSubscriber
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CuratedProductSortViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-04.
 */
class AffiliateCuratedProductPresenter
@Inject constructor(
        private val getCuratedProductListUseCase: GetCuratedProductListUseCase,
        private val getCuratedProductSortUseCase: GetCuratedProductSortUseCase
) : BaseDaggerPresenter<AffiliateCuratedProductContract.View>(), AffiliateCuratedProductContract.Presenter {

    override fun loadCuratedProductByType(type: Int?, cursor: String, sort: Int?, startDate: Date?, endDate: Date?) {
        if (cursor.isEmpty()) view.showLoading()
        getCuratedProductListUseCase.run {
            clearRequest()
            addRequest(getRequest(type, cursor, sort, startDate, endDate))
            execute(GetCuratedProductListSubscriber(type, view, cursor))
        }
    }

    override fun loadSortOptions() {
        getCuratedProductSortUseCase.run {
            clearRequest()
            addRequest(getRequest())
            execute(object: Subscriber<GraphqlResponse>() {
                override fun onNext(response: GraphqlResponse) {
                    val data = response.getData<AffiliateProductSortQuery>(AffiliateProductSortQuery::class.java)
                    view.onGetSortOptions(getSortList(data))
                }

                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {

                }
            })
        }
    }

    override fun reloadSortOptions(sortList: List<CuratedProductSortViewModel>, selectedId: Int?) {
        view.onGetSortOptions(getSortList(sortList, selectedId))
    }

    override fun detachView() {
        super.detachView()
        getCuratedProductListUseCase.unsubscribe()
        getCuratedProductSortUseCase.unsubscribe()
    }

    private fun getSortList(query: AffiliateProductSortQuery): List<CuratedProductSortViewModel> {
        return query.affiliatedProductSort.sorts.map { option ->
            CuratedProductSortViewModel(
                    option.sortVal,
                    option.text,
                    false
            )
        }
    }

    private fun getSortList(existingSortList: List<CuratedProductSortViewModel>, selectedId: Int?): List<CuratedProductSortViewModel> {
        return existingSortList.map {
            it.copy(isChecked = it.id == selectedId)
        }
    }
}