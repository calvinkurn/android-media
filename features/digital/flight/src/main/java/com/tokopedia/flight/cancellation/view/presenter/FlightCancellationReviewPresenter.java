package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.domain.FlightCancellationEstimateRefundUseCase;
import com.tokopedia.flight.cancellation.domain.FlightCancellationRequestUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReviewContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by furqan on 11/04/18.
 */

public class FlightCancellationReviewPresenter extends BaseDaggerPresenter<FlightCancellationReviewContract.View>
        implements FlightCancellationReviewContract.Presenter {

    private FlightCancellationRequestUseCase flightCancellationRequestUseCase;
    private FlightCancellationEstimateRefundUseCase flightCancellationEstimateRefundUseCase;
    private UserSessionInterface userSession;

    @Inject
    public FlightCancellationReviewPresenter(FlightCancellationRequestUseCase flightCancellationRequestUseCase,
                                             FlightCancellationEstimateRefundUseCase flightCancellationEstimateRefundUseCase,
                                             UserSessionInterface userSession) {
        this.flightCancellationRequestUseCase = flightCancellationRequestUseCase;
        this.flightCancellationEstimateRefundUseCase = flightCancellationEstimateRefundUseCase;
        this.userSession = userSession;
    }

    @Override
    public void onViewCreated() {
        FlightCancellationWrapperViewModel flightCancellationWrapperViewModel = getView().getCancellationWrapperViewModel();

        Iterator<FlightCancellationViewModel> iterator = flightCancellationWrapperViewModel.getGetCancellations().iterator();

        while (iterator.hasNext()) {
            FlightCancellationViewModel item = iterator.next();
            if (item.getPassengerViewModelList() == null || item.getPassengerViewModelList().size() == 0) {
                iterator.remove();
            }
        }

        getView().setCancellationWrapperViewModel(flightCancellationWrapperViewModel);

        actionFetchEstimateRefund();
    }

    @Override
    public void onRetryFetchEstimate() {
        actionFetchEstimateRefund();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void requestCancellation() {
        getView().showLoading();
        FlightCancellationWrapperViewModel viewModel = getView().getCancellationWrapperViewModel();

        String reason = (viewModel.getCancellationReasonAndAttachment() != null) ?
                viewModel.getCancellationReasonAndAttachment().getReason() : null;
        String reasonId = (viewModel.getCancellationReasonAndAttachment() != null) ?
                viewModel.getCancellationReasonAndAttachment().getReasonId() : null;
        List<FlightCancellationAttachmentViewModel> attachmentViewModelList = (viewModel.getCancellationReasonAndAttachment() != null) ?
                viewModel.getCancellationReasonAndAttachment().getAttachments() : null;

        flightCancellationRequestUseCase.execute(
                flightCancellationRequestUseCase.createRequest(
                        getView().getInvoiceId(),
                        reason,
                        reasonId,
                        attachmentViewModelList,
                        viewModel.getGetCancellations()
                ),
                new Subscriber<CancellationRequestEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().showCancellationError(throwable);
                        getView().hideLoading();
                    }

                    @Override
                    public void onNext(CancellationRequestEntity cancellationRequestEntity) {
                        getView().hideLoading();
                        if (isRefundable()) {
                            getView().showSuccessDialog(R.string.flight_cancellation_review_dialog_refundable_success_description);
                        } else {
                            getView().showSuccessDialog(R.string.flight_cancellation_review_dialog_non_refundable_success_description);
                        }
                    }
                }
        );
    }

    private void setRefundView() {
        if (isRefundable()) {
            if (getView().getCancellationWrapperViewModel().getCancellationReasonAndAttachment().isShowEstimateRefund()) {
                getView().showEstimateValue();
                getView().hideRefundDetail();
            } else {
                getView().hideEstimateValue();
                getView().showRefundDetail(R.string.flight_cancellation_review_refund_to_email_detail);
            }
        } else {
            getView().hideEstimateValue();
            getView().showRefundDetail(R.string.flight_cancellation_review_no_refund_detail);
        }
    }

    private boolean isRefundable() {
        boolean isRefundable = false;

        for (FlightCancellationViewModel item : getView().getCancellationWrapperViewModel().getGetCancellations()) {
            if (item.getFlightCancellationJourney().isRefundable()) {
                isRefundable = true;
                break;
            }
        }

        return isRefundable;
    }

    private void actionFetchEstimateRefund() {
        getView().showLoading();

        FlightCancellationWrapperViewModel viewModel = getView().getCancellationWrapperViewModel();

        String reason = (viewModel.getCancellationReasonAndAttachment() != null) ?
                viewModel.getCancellationReasonAndAttachment().getReason() : null;
        String reasonId = (viewModel.getCancellationReasonAndAttachment() != null) ?
                viewModel.getCancellationReasonAndAttachment().getReasonId() : null;

        flightCancellationEstimateRefundUseCase.execute(
                flightCancellationEstimateRefundUseCase.createRequestParam(
                        getView().getCancellationWrapperViewModel().getInvoice(),
                        userSession.getUserId(),
                        getView().getCancellationWrapperViewModel().getGetCancellations(),
                        reason,
                        Integer.parseInt(reasonId)
                ),
                new Subscriber<EstimateRefundResultEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached() && !isUnsubscribed()) {
                            getView().hideLoading();
                            getView().showErrorFetchEstimateRefund(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
                        }
                    }

                    @Override
                    public void onNext(EstimateRefundResultEntity estimateRefundResultEntity) {
                        getView().hideLoading();
                        getView().getCancellationWrapperViewModel().getCancellationReasonAndAttachment()
                                .setEstimateRefund(estimateRefundResultEntity.getAttribute().getValueNumeric());
                        getView().getCancellationWrapperViewModel().getCancellationReasonAndAttachment()
                                .setEstimateFmt(estimateRefundResultEntity.getAttribute().getValue());
                        getView().getCancellationWrapperViewModel().getCancellationReasonAndAttachment()
                                .setShowEstimateRefund(estimateRefundResultEntity.getAttribute().isShowEstimate());
                        getView().renderView();

                        setRefundView();
                    }
                }
        );
    }
}
