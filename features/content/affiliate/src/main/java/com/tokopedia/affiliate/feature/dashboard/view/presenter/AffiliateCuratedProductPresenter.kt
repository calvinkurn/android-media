package com.tokopedia.affiliate.feature.dashboard.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.dashboard.data.pojo.AffiliateProductSortQuery
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetCuratedProductSortUseCase
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardLoadMoreUseCase
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateCuratedProductContract
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetDashboardLoadMoreSubscriber
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CuratedProductSortViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-04.
 */
class AffiliateCuratedProductPresenter
@Inject constructor(
        private val getDashboardLoadMoreUseCase: GetDashboardLoadMoreUseCase,
        private val getCuratedProductSortUseCase: GetCuratedProductSortUseCase
) : BaseDaggerPresenter<AffiliateCuratedProductContract.View>(), AffiliateCuratedProductContract.Presenter {

    override fun loadProductBoughtByType(type: Int?, cursor: String, sort: Int) {
        if (cursor.isEmpty()) view.showLoading()
        getDashboardLoadMoreUseCase.run {
            clearRequest()
            addRequest(getRequest(type, cursor, sort))
            execute(GetDashboardLoadMoreSubscriber(type, view))
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

    override fun reloadSortOptions(sortList: List<CuratedProductSortViewModel>, selectedId: Int) {
        view.onGetSortOptions(getSortList(sortList, selectedId))
    }

    override fun detachView() {
        super.detachView()
        getDashboardLoadMoreUseCase.unsubscribe()
    }

    private fun getSortList(query: AffiliateProductSortQuery): List<CuratedProductSortViewModel> {
        return query.affiliatedProductSort.sorts.map { option ->
            CuratedProductSortViewModel(
                    option.sortVal,
                    option.text,
                    option.sortVal == 1
            )
        }
    }

    private fun getSortList(existingSortList: List<CuratedProductSortViewModel>, selectedId: Int): List<CuratedProductSortViewModel> {
        return existingSortList.map {
            it.copy(isChecked = it.id == selectedId)
        }
    }
}