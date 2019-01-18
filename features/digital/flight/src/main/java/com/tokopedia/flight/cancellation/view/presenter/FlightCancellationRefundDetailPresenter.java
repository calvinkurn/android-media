package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.domain.FlightCancellationEstimateRefundUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationRefundDetailContract;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationRefundDetailContract.Presenter;
import com.tokopedia.flight.common.util.FlightErrorUtil;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/9/18.
 */

public class FlightCancellationRefundDetailPresenter extends BaseDaggerPresenter<FlightCancellationRefundDetailContract.View> implements Presenter {
    private FlightCancellationEstimateRefundUseCase flightCancellationEstimateRefundUseCase;
    private UserSession userSession;

    @Inject
    public FlightCancellationRefundDetailPresenter(FlightCancellationEstimateRefundUseCase flightCancellationEstimateRefundUseCase, UserSession userSession) {
        this.flightCancellationEstimateRefundUseCase = flightCancellationEstimateRefundUseCase;
        this.userSession = userSession;
    }

    @Override
    public void initialize() {
        actionFetchEstimateRefund();
    }

    @Override
    public void onRetryFetchEstimate() {
        actionFetchEstimateRefund();
    }

    @Override
    public void onNextButtonClicked() {

    }

    private void actionFetchEstimateRefund() {
        getView().showLoading();
        getView().hideFullPageContainer();
        flightCancellationEstimateRefundUseCase.execute(
                flightCancellationEstimateRefundUseCase.createRequestParam(
                        getView().getCancellationViewModel().getInvoice(),
                        userSession.getUserId(),
                        getView().getCancellationViewModel().getGetCancellations(),
                        "", 0
                ),
                new Subscriber<EstimateRefundResultEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached() && !isUnsubscribed()) {
                            getView().hideLoading();
                            getView().showErrorFetchEstimateRefund(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
                        }
                    }

                    @Override
                    public void onNext(EstimateRefundResultEntity estimateRefundResultEntity) {
                        getView().hideLoading();
                        getView().showFullPageContainer();
                        getView().getCancellationViewModel().getCancellationReasonAndAttachment().setEstimateRefund(estimateRefundResultEntity.getAttribute().getValueNumeric());
                        getView().getCancellationViewModel().getCancellationReasonAndAttachment().setEstimateFmt(estimateRefundResultEntity.getAttribute().getValue());
                        getView().renderTotalRefund(estimateRefundResultEntity.getAttribute().getValue());
                    }
                }
        );
    }
}
