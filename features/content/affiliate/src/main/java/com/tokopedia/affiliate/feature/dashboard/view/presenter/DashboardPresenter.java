package com.tokopedia.affiliate.feature.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.common.domain.usecase.CheckAffiliateUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardLoadMoreUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardUseCase;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.CheckAffiliateSubscriber;
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetDashboardLoadMoreSubscriber;
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetDashboardSubscriber;

import javax.inject.Inject;

/**
 * @author by yfsx on 13/09/18.
 */
public class DashboardPresenter extends BaseDaggerPresenter<DashboardContract.View> implements DashboardContract.Presenter {

    private final GetDashboardUseCase getDashboardUseCase;
    private final GetDashboardLoadMoreUseCase getDashboardLoadMoreUseCase;
    private CheckAffiliateUseCase checkAffiliateUseCase;

    @Inject
    public DashboardPresenter(
            GetDashboardUseCase getDashboardUseCase,
                              GetDashboardLoadMoreUseCase getDashboardLoadMoreUseCase,
            CheckAffiliateUseCase checkAffiliateUseCase) {
        this.getDashboardUseCase = getDashboardUseCase;
        this.getDashboardLoadMoreUseCase = getDashboardLoadMoreUseCase;
        this.checkAffiliateUseCase = checkAffiliateUseCase;
    }

    @Override
    public void loadDashboardItem(boolean isPullToRefresh) {
        if (!isPullToRefresh) getView().showLoading();
        getDashboardUseCase.clearRequest();
        getDashboardUseCase.addRequest(getDashboardUseCase.getRequest());
        getDashboardUseCase.execute(new GetDashboardSubscriber(getView()));
    }

    @Override
    public void loadMoreDashboardItem(String cursor) {
        getDashboardLoadMoreUseCase.clearRequest();
        getDashboardLoadMoreUseCase.addRequest(getDashboardLoadMoreUseCase.getRequest(cursor));
        getDashboardLoadMoreUseCase.execute(new GetDashboardLoadMoreSubscriber(getView()));
    }

    @Override
    public void checkAffiliate() {
        checkAffiliateUseCase.execute(new CheckAffiliateSubscriber(getView()));
    }

    @Override
    public void detachView() {
        super.detachView();
        getDashboardUseCase.unsubscribe();
        getDashboardLoadMoreUseCase.unsubscribe();
        checkAffiliateUseCase.unsubscribe();
    }
}
