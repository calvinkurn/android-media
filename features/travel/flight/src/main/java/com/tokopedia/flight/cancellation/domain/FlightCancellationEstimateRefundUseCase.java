package com.tokopedia.flight.cancellation.domain;

import com.tokopedia.flight.cancellation.data.cloud.entity.EstimateRefundResultEntity;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationDetailRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundAttribute;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightEstimateRefundRequest;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @author by alvarisi on 4/9/18.
 */

public class FlightCancellationEstimateRefundUseCase extends UseCase<EstimateRefundResultEntity> {
    private static final String PARAM_REQUEST = "PARAM_REQUEST";
    private static final String DEFAULT_TYPE = "order_cancel_estimate";
    private FlightRepository flightRepository;

    public FlightCancellationEstimateRefundUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<EstimateRefundResultEntity> createObservable(RequestParams requestParams) {
        return flightRepository.estimateRefund((FlightEstimateRefundRequest) requestParams.getObject(PARAM_REQUEST));
    }

    public RequestParams createRequestParam(String invoiceId, String userId, List<FlightCancellationViewModel> journeyCancellations,
                                            String reason, int reasonType){
        RequestParams requestParams = RequestParams.create();
        FlightEstimateRefundRequest requestBody = new FlightEstimateRefundRequest();
        requestBody.setType(DEFAULT_TYPE);
        FlightEstimateRefundAttribute attribute = new FlightEstimateRefundAttribute();
        attribute.setUserId(Long.parseLong(userId));
        attribute.setInvoiceId(invoiceId);
        attribute.setReason(reason);
        attribute.setReasonType(reasonType);
        List<FlightCancellationDetailRequestBody> details = transformIntoDetails(journeyCancellations);
        attribute.setDetails(details);
        requestBody.setAttributes(attribute);
        requestParams.putObject(PARAM_REQUEST, requestBody);
        return requestParams;
    }

    private List<FlightCancellationDetailRequestBody> transformIntoDetails(List<FlightCancellationViewModel> journeyCancellations) {
        List<FlightCancellationDetailRequestBody> detailRequestBodies = new ArrayList<>();
        for (FlightCancellationViewModel viewModel : journeyCancellations ){
            for (FlightCancellationPassengerViewModel passengerViewModel : viewModel.getPassengerViewModelList()) {
                FlightCancellationDetailRequestBody detailRequestBody = new FlightCancellationDetailRequestBody();
                detailRequestBody.setJourneyId(Long.parseLong(viewModel.getFlightCancellationJourney().getJourneyId()));
                detailRequestBody.setPassengerId(Long.parseLong(passengerViewModel.getPassengerId()));
                detailRequestBodies.add(detailRequestBody);
            }
        }
        return detailRequestBodies;
    }
}
