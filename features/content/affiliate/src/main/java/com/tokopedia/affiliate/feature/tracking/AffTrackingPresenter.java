package com.tokopedia.affiliate.feature.tracking;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.tracking.domain.interactor.GetByMeUseCase;
import com.tokopedia.affiliate.feature.tracking.domain.model.Data;
import com.tokopedia.affiliate.feature.tracking.view.AffContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import javax.inject.Inject;

import rx.Subscriber;

public class AffTrackingPresenter extends BaseDaggerPresenter<AffContract.View>
        implements AffContract.Presenter {

    @Inject
    GetByMeUseCase getByMeUseCase;

    @Override
    public void getTrackingUrl(String affName, String urlKey) {
        getByMeUseCase.createObservable(affName, urlKey, getSubscriber());
    }

    @NonNull
    protected Subscriber<GraphqlResponse> getSubscriber() {
        return new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
                if(isViewAttached()) {
                    getView().finishActivity();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if(isViewAttached()) {
                    getView().handleError();
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (isViewAttached()) {
                    if (graphqlResponse != null && graphqlResponse.getData(Data.class) != null) {
                        Data data = graphqlResponse.getData(Data.class);
                        if (data != null) {
                            getView().handleLink(data.getBymeGetRealURL().getRealURL());
                        } else {
                            getView().handleError();
                        }
                    } else {
                        getView().handleError();
                    }
                }
            }
        };
    }
}
