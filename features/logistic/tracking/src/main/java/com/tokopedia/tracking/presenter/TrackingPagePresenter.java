package com.tokopedia.tracking.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.tracking.usecase.TrackCourierUseCase;
import com.tokopedia.tracking.view.ITrackingPageFragment;
import com.tokopedia.tracking.viewmodel.TrackingViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackingPagePresenter extends BaseDaggerPresenter implements ITrackingPagePresenter {

    private TrackCourierUseCase useCase;
    private UserSession userSession;
    private ITrackingPageFragment view;

    @Inject
    public TrackingPagePresenter(TrackCourierUseCase useCase,
                                 UserSession userSession,
                                 ITrackingPageFragment view) {
        this.useCase = useCase;
        this.userSession = userSession;
        this.view = view;
    }


    @Override
    public void onGetTrackingData(String orderId) {
        view.showMainLoadingPage();
        TKPDMapParam<String, String> request = new TKPDMapParam<>();
        request.put("order_id", orderId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putAllString(
                AuthUtil.generateParamsNetwork(
                        userSession.getUserId(), userSession.getDeviceId(), request)
        );
        useCase.execute(requestParams, trackingResultSubscriber());
    }

    @Override
    public void onDetach() {
        useCase.unsubscribe();
    }

    private Subscriber<TrackingViewModel> trackingResultSubscriber() {
        return new Subscriber<TrackingViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.closeMainLoadingPage();
                view.showError(e.getMessage());
            }

            @Override
            public void onNext(TrackingViewModel trackingViewModel) {
                view.closeMainLoadingPage();
                view.populateView(trackingViewModel);
            }
        };
    }


}
