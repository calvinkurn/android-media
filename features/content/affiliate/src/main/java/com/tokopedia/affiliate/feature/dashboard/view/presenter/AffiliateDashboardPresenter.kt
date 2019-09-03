package com.tokopedia.affiliate.feature.dashboard.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.common.domain.usecase.CheckAffiliateUseCase
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardUseCase
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.CheckAffiliateSubscriber
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetAffiliateDashboardSubscriber
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-02.
 */
class AffiliateDashboardPresenter
@Inject constructor(
        private val getDashboardUseCase: GetDashboardUseCase,
        private val checkAffiliateUseCase: CheckAffiliateUseCase
) : BaseDaggerPresenter<AffiliateDashboardContract.View>(), AffiliateDashboardContract.Presenter {

    override fun checkAffiliate() {
        checkAffiliateUseCase.execute(CheckAffiliateSubscriber(view))
    }

    override fun loadDashboardDetail() {
        getDashboardUseCase.run {
            clearRequest()
            addRequest(request)
            execute(GetAffiliateDashboardSubscriber(view))
        }
    }
}