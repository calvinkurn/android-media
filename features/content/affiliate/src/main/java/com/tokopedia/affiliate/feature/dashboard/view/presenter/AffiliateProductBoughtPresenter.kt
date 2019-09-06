package com.tokopedia.affiliate.feature.dashboard.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardLoadMoreUseCase
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateCuratedProductContract
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetDashboardLoadMoreSubscriber
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-04.
 */
class AffiliateProductBoughtPresenter
@Inject constructor(
        private val getDashboardLoadMoreUseCase: GetDashboardLoadMoreUseCase
) : BaseDaggerPresenter<AffiliateCuratedProductContract.View>(), AffiliateCuratedProductContract.Presenter {

    override fun loadProductBoughtByType(type: Int?, cursor: String) {
        getDashboardLoadMoreUseCase.run {
            clearRequest()
            addRequest(getRequest(type, cursor))
            execute(GetDashboardLoadMoreSubscriber(type, view))
        }
    }

    override fun detachView() {
        super.detachView()
        getDashboardLoadMoreUseCase.unsubscribe()
    }
}