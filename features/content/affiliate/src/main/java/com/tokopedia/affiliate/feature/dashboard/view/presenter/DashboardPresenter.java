package com.tokopedia.affiliate.feature.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardLoadMoreUseCase;
import com.tokopedia.affiliate.feature.dashboard.domain.usecase.GetDashboardUseCase;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetDashboardLoadMoreSubscriber;
import com.tokopedia.affiliate.feature.dashboard.view.subscriber.GetDashboardSubscriber;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import javax.inject.Inject;

/**
 * @author by yfsx on 13/09/18.
 */
public class DashboardPresenter extends BaseDaggerPresenter<DashboardContract.View> implements DashboardContract.Presenter {

    private DashboardContract.View mainView;
    private final GetDashboardUseCase getDashboardUseCase;
    private final GetDashboardLoadMoreUseCase getDashboardLoadMoreUseCase;

//    @Inject
    public DashboardPresenter(
            GetDashboardUseCase getDashboardUseCase,
                              GetDashboardLoadMoreUseCase getDashboardLoadMoreUseCase) {
        this.mainView = getView();
        this.getDashboardUseCase = getDashboardUseCase;
        this.getDashboardLoadMoreUseCase = getDashboardLoadMoreUseCase;
    }

    @Override
    public void loadDashboardItem() {
        mainView.showLoading();
        getDashboardUseCase.clearRequest();
        getDashboardUseCase.addRequest(getDashboardUseCase.getRequest());
        getDashboardUseCase.execute(new GetDashboardSubscriber(mainView));
    }

    @Override
    public void loadMoreDashboardItem(String cursor) {
        mainView.showLoading();
        getDashboardLoadMoreUseCase.clearRequest();
        getDashboardLoadMoreUseCase.addRequest(getDashboardLoadMoreUseCase.getRequest());
        getDashboardLoadMoreUseCase.execute(new GetDashboardLoadMoreSubscriber(mainView));
    }
}
