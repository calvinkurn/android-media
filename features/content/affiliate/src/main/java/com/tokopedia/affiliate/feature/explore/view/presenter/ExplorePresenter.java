package com.tokopedia.affiliate.feature.explore.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.explore.domain.usecase.ExploreUseCase;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;
import com.tokopedia.affiliate.feature.explore.view.subscriber.GetExploreFirstSubscriber;
import com.tokopedia.affiliate.feature.explore.view.subscriber.GetExploreLoadMoreSubscriber;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;

import javax.inject.Inject;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExplorePresenter extends BaseDaggerPresenter<ExploreContract.View> implements ExploreContract.Presenter {

    private ExploreUseCase exploreUseCase;

    @Inject
    public ExplorePresenter(ExploreUseCase exploreUseCase) {
        this.exploreUseCase = exploreUseCase;
    }

    @Override
    public void getFirstData(ExploreParams exploreParams, boolean isPullToRefresh) {
        if (!isPullToRefresh) getView().showLoading();
        exploreUseCase.clearRequest();
        exploreUseCase.addRequest(exploreUseCase.getRequest(exploreParams));
        exploreUseCase.execute(
                new GetExploreFirstSubscriber(
                        getView(),
                        !TextUtils.isEmpty(exploreParams.getKeyword()))
        );

    }

    @Override
    public void loadMoreData(ExploreParams exploreParams) {
        exploreUseCase.clearRequest();
        exploreUseCase.addRequest(exploreUseCase.getRequest(exploreParams));
        exploreUseCase.execute(new GetExploreLoadMoreSubscriber(getView()));
    }

    @Override
    public void detachView() {
        super.detachView();
        exploreUseCase.unsubscribe();
    }
}
