package com.tokopedia.topads.dashboard.view.presenter;


import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.topads.dashboard.data.model.DataCredit;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDataCreditUseCase;
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddCreditView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsAddCreditPresenter extends BaseDaggerPresenter<TopAdsAddCreditView> {

    private TopAdsGetDataCreditUseCase useCase;

    @Inject
    public TopAdsAddCreditPresenter(TopAdsGetDataCreditUseCase useCase) {
        this.useCase = useCase;
    }

    public void populateCreditList() {
        useCase.execute(new Subscriber<List<DataCredit>>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()){
                    getView().onLoadCreditListError();
                }
            }

            @Override
            public void onNext(List<DataCredit> dataCredits) {
                if (isViewAttached()){
                    getView().onCreditListLoaded(dataCredits);
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        if (useCase != null){
            useCase.unsubscribe();
            useCase = null;
        }
    }
}