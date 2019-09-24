package com.tokopedia.affiliate.feature.dashboard.view.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardBalance
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardHeaderPojo
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.ShareableByMeProfileViewModel
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.feedcomponent.data.pojo.profileheader.BymeProfileHeader
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by jegul on 2019-09-03.
 */
class GetAffiliateDashboardSubscriber(
        private val mainView: AffiliateDashboardContract.View
) : Subscriber<GraphqlResponse>() {

    override fun onNext(response: GraphqlResponse) {
        val query = response.getData<DashboardQuery>(DashboardQuery::class.java)
        mainView.onSuccessGetDashboardItem(
                mappingHeader(query.affiliateStats, query.balance),
                mappingByMeProfile(query.header)
        )
        mainView.hideLoading()
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        mainView.onErrorGetDashboardItem(ErrorHandler.getErrorMessage(mainView.ctx, e))
        mainView.hideLoading()
    }

    private fun mappingHeader(pojo: DashboardHeaderPojo, balance: DashboardBalance) = pojo.let {
        DashboardHeaderViewModel(
                it.totalCommission,
                CurrencyFormatUtil.convertPriceValueToIdrFormat(balance.sellerUsable, true),
                it.profileView,
                it.productClick,
                it.productSold,
                it.productCount
        )
    }

    private fun mappingByMeProfile(header: BymeProfileHeader) = header.let {
        ShareableByMeProfileViewModel(
                it.profile.affiliateName,
                it.profile.avatar,
                it.profile.link
        )
    }
}