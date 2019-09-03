package com.tokopedia.affiliate.feature.dashboard.view.subscriber

import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardHeaderPojo
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by jegul on 2019-09-03.
 */
class GetAffiliateDashboardSubscriber(
        private val mainView: AffiliateDashboardContract.View
) : Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {
        mainView.hideLoading()
        val query = response.getData<DashboardQuery>(DashboardQuery::class.java)
        mainView.onSuccessGetDashboardItem(
                mappingHeader(query.affiliateStats)
        )
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        mainView.hideLoading()
    }

    private fun mappingHeader(pojo: DashboardHeaderPojo) = pojo.let {
        DashboardHeaderViewModel(
                it.totalCommission,
                it.profileView,
                it.productClick,
                it.productSold
        )
    }
}