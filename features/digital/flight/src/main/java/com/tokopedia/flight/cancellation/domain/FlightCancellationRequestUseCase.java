package com.tokopedia.flight.cancellation.domain;

import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationDetailRequestBody;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestAttachment;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestAttribute;
import com.tokopedia.flight.cancellation.data.cloud.requestbody.FlightCancellationRequestBody;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
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
                                       List<FlightCancellationAttachmentViewModel> attachments,
                                       List<FlightCancellationViewModel> journeyCancellations) {
        RequestParams requestParams = RequestParams.create();

        FlightCancellationRequestAttribute flightCancellationRequestAttribute = new FlightCancellationRequestAttribute();
        flightCancellationRequestAttribute.setInvoiceId(invoiceId);
        flightCancellationRequestAttribute.setReason(reason);
        flightCancellationRequestAttribute.setReasonId(Integer.parseInt(reasonId));
        flightCancellationRequestAttribute.setAttachments(transformIntoRequestAttachments(attachments));
        flightCancellationRequestAttribute.setDetails(transformIntoDetails(journeyCancellations));

        FlightCancellationRequestBody flightCancellationRequestBody = new FlightCancellationRequestBody();
        flightCancellationRequestBody.setType(DEFAULT_FLIGHT_CANCEL_REQUEST_TYPE);
        flightCancellationRequestBody.setAttributes(flightCancellationRequestAttribute);

        requestParams.putObject(FLIGHT_CANCELLATION_REQUEST_KEY, flightCancellationRequestBody);

        return requestParams;
    }

    private List<FlightCancellationRequestAttachment> transformIntoRequestAttachments(List<FlightCancellationAttachmentViewModel> attachments) {
        if (attachments != null && attachments.size() > 0) {
            List<FlightCancellationRequestAttachment> requestAttachments = new ArrayList<>();

            FlightCancellationRequestAttachment attachment = new FlightCancellationRequestAttachment();
            attachment.setDocsId(DEFAULT_DOCS_ID);
            attachment.setDocsLinks(new ArrayList<>());

            for (FlightCancellationAttachmentViewModel item : attachments) {
                attachment.getDocsLinks().add(item.getImageurl());
            }

            requestAttachments.add(attachment);

            return requestAttachments;
        } else {
            return new ArrayList<>();
        }
    }

    private List<FlightCancellationDetailRequestBody> transformIntoDetails(List<FlightCancellationViewModel> journeyCancellations) {
        List<FlightCancellationDetailRequestBody> detailRequestBodies = new ArrayList<>();
        for (FlightCancellationViewModel viewModel : journeyCancellations) {
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
