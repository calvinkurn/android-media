package com.tokopedia.affiliate.feature.tracking.view.presenter;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.tracking.domain.interactor.GetByMeUseCase;
import com.tokopedia.affiliate.feature.tracking.domain.model.Data;
import com.tokopedia.affiliate.feature.tracking.view.contract.AffContract;
import com.tokopedia.affiliate.feature.tracking.view.subscriber.AffTrackingSubscriber;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import javax.inject.Inject;

import rx.Subscriber;

public class AffTrackingPresenter extends BaseDaggerPresenter<AffContract.View>
        implements AffContract.Presenter {

    private GetByMeUseCase getByMeUseCase;

    @Inject
    AffTrackingPresenter(GetByMeUseCase getByMeUseCase) {
        this.getByMeUseCase = getByMeUseCase;
    }

    @Override
    public void getTrackingUrl(String affName, String urlKey) {
        getByMeUseCase.createObservable(affName, urlKey, new AffTrackingSubscriber(getView()));
    }

    @Override
    public void detachView() {
        super.detachView();
        getByMeUseCase.unsubscribe();
    }
}
