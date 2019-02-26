package com.tokopedia.home.beranda.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.home.beranda.domain.interactor.GetHomeFeedUseCase;
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber;

import javax.inject.Inject;

public class HomeFeedPresenter extends BaseDaggerPresenter<HomeFeedContract.View> implements HomeFeedContract.Presenter {

    @Inject
    GetHomeFeedUseCase getHomeFeedUseCase;

    public void loadData(int recomId, int count, int page) {
        getHomeFeedUseCase.execute(
                getHomeFeedUseCase.getHomeFeedParam(recomId, count, page),
                new GetHomeFeedsSubscriber(getView()));
    }

    @Override
    public void detachView() {
        super.detachView();
        getHomeFeedUseCase.unsubscribe();
    }
}
