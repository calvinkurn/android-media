package com.tokopedia.flight.cancellation.domain.mapper;

import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 26/10/18.
 */

public class FlightCancellationReasonsViewModelMapper {

    @Inject
    public FlightCancellationReasonsViewModelMapper() {
    }

    public List<FlightCancellationReasonModel> transform(List<Reason> reasonList) {
        List<FlightCancellationReasonModel> data = new ArrayList<>();

        if (reasonList != null) {
            for (Reason reason : reasonList) {
                data.add(transform(reason));
            }
        }

        return data;
    }

    public FlightCancellationReasonModel transform(Reason reason) {
        FlightCancellationReasonModel data = new FlightCancellationReasonModel();

        data.setId(reason.getId());
        data.setDetail(reason.getTitle());
        data.setRequiredDocs(reason.getRequiredDocs());

        return data;
    }
}
