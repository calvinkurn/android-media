package com.tokopedia.affiliate.feature.dashboard.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.common.domain.usecase.CheckAffiliateUseCase
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardLoadMoreUseCase
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateProductBoughtContract
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.CheckAffiliateSubscriber
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetAffiliateDashboardSubscriber
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-04.
 */
class AffiliateProductBoughtPresenter
@Inject constructor(
        private val getDashboardLoadMoreUseCase: GetDashboardLoadMoreUseCase
) : BaseDaggerPresenter<AffiliateProductBoughtContract.View>(), AffiliateProductBoughtContract.Presenter {

    override fun loadProductBoughtByType(type: Int, cursor: String) {
        getDashboardLoadMoreUseCase.run {
            clearRequest()
            addRequest(getRequest(type, cursor))
        }
    }
}