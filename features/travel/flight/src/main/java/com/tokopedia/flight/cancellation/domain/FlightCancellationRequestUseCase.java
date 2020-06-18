package com.tokopedia.flight.cancellation.domain;

import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationDetailRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestAttribute;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 11/04/18.
 */

public class FlightCancellationRequestUseCase extends UseCase<CancellationRequestEntity> {

    private static final String DEFAULT_FLIGHT_CANCEL_REQUEST_TYPE = "order_cancel_request";
    private static final String FLIGHT_CANCELLATION_REQUEST_KEY = "FLIGHT_CANCELLATION_REQUEST_KEY";

    private static final long DEFAULT_DOCS_ID = 1;

    private FlightRepository flightRepository;

    @Inject
    public FlightCancellationRequestUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<CancellationRequestEntity> createObservable(RequestParams requestParams) {
        return flightRepository.cancellationRequest((FlightCancellationRequestBody) requestParams.getObject(FLIGHT_CANCELLATION_REQUEST_KEY));
    }

    public RequestParams createRequest(String invoiceId, String reason, String reasonId,
                                       List<FlightCancellationModel> journeyCancellations) {
        RequestParams requestParams = RequestParams.create();

        FlightCancellationRequestAttribute flightCancellationRequestAttribute = new FlightCancellationRequestAttribute();
        flightCancellationRequestAttribute.setInvoiceId(invoiceId);
        flightCancellationRequestAttribute.setReason(reason);
        flightCancellationRequestAttribute.setReasonId(Integer.parseInt(reasonId));
        flightCancellationRequestAttribute.setAttachments(new ArrayList<>());
        flightCancellationRequestAttribute.setDetails(transformIntoDetails(journeyCancellations));

        FlightCancellationRequestBody flightCancellationRequestBody = new FlightCancellationRequestBody();
        flightCancellationRequestBody.setType(DEFAULT_FLIGHT_CANCEL_REQUEST_TYPE);
        flightCancellationRequestBody.setAttributes(flightCancellationRequestAttribute);

        requestParams.putObject(FLIGHT_CANCELLATION_REQUEST_KEY, flightCancellationRequestBody);

        return requestParams;
    }

    private List<FlightCancellationDetailRequestBody> transformIntoDetails(List<FlightCancellationModel> journeyCancellations) {
        List<FlightCancellationDetailRequestBody> detailRequestBodies = new ArrayList<>();
        for (FlightCancellationModel viewModel : journeyCancellations) {
            for (FlightCancellationPassengerModel passengerViewModel : viewModel.getPassengerViewModelList()) {
                FlightCancellationDetailRequestBody detailRequestBody = new FlightCancellationDetailRequestBody();
                detailRequestBody.setJourneyId(Long.parseLong(viewModel.getFlightCancellationJourney().getJourneyId()));
                detailRequestBody.setPassengerId(Long.parseLong(passengerViewModel.getPassengerId()));
                detailRequestBodies.add(detailRequestBody);
            }
        }
        return detailRequestBodies;
    }
}
