package com.tokopedia.tracking.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.tracking.usecase.GetRetryAvailability;
import com.tokopedia.tracking.usecase.RetryPickup;
import com.tokopedia.tracking.usecase.TrackCourierUseCase;
import com.tokopedia.tracking.usecase.entity.RetryAvailability;
import com.tokopedia.tracking.usecase.entity.RetryAvailabilityResponse;
import com.tokopedia.tracking.usecase.entity.RetryBookingResponse;
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
    private GetRetryAvailability retryAvailUsecase;
    private RetryPickup retryPickupUsecase;
    private ITrackingPageFragment view;

    @Inject
    public TrackingPagePresenter(TrackCourierUseCase useCase,
                                 GetRetryAvailability getRetryUsecase,
                                 RetryPickup retryPickupUsecase,
                                 UserSession userSession,
                                 ITrackingPageFragment view) {
        this.useCase = useCase;
        this.userSession = userSession;
        this.retryAvailUsecase = getRetryUsecase;
        this.retryPickupUsecase = retryPickupUsecase;
        this.view = view;
    }


    @Override
    public void onGetTrackingData(String orderId) {
        view.showLoading();
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
    public void onGetRetryAvailability(String orderId) {
        retryAvailUsecase.execute(orderId)
                .subscribe(new Subscriber<RetryAvailabilityResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showSoftError(e);
                    }

                    @Override
                    public void onNext(RetryAvailabilityResponse retryAvailabilityResponse) {
                        RetryAvailability avail = retryAvailabilityResponse.getRetryAvailability();
                        long deadline = Long.parseLong(avail.getDeadlineRetryUnixtime());
                        if (avail.getShowRetryButton() && avail.getAvailabilityRetry()) {
                            view.setRetryButton(true, 0L);
                        } else if (!avail.getAvailabilityRetry()) {
                            view.setRetryButton(false, deadline);
                        } else view.setRetryButton(false, 0L);
                    }
                });
    }

    @Override
    public void onRetryPickup(String orderId) {
        retryPickupUsecase.execute(orderId)
                .subscribe(new Subscriber<RetryBookingResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e);
                    }

                    @Override
                    public void onNext(RetryBookingResponse retryBookingResponse) {
                        view.startSuccessCountdown();
                    }
                });
    }

    @Override
    public void onDetach() {
        useCase.unsubscribe();
        retryAvailUsecase.unsubscribe();
        retryPickupUsecase.unsubscribe();
    }

    private Subscriber<TrackingViewModel> trackingResultSubscriber() {
        return new Subscriber<TrackingViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideLoading();
                view.showError(e);
            }

            @Override
            public void onNext(TrackingViewModel trackingViewModel) {
                view.hideLoading();
                view.populateView(trackingViewModel);
            }
        };
    }


}
