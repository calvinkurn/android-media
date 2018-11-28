package com.tokopedia.flight.cancellation.domain.mapper;

import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;

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

    public List<FlightCancellationReasonViewModel> transform(List<Reason> reasonList) {
        List<FlightCancellationReasonViewModel> data = new ArrayList<>();

        if (reasonList != null) {
            for (Reason reason : reasonList) {
                data.add(transform(reason));
            }
        }

        return data;
    }

    public FlightCancellationReasonViewModel transform(Reason reason) {
        FlightCancellationReasonViewModel data = new FlightCancellationReasonViewModel();

        data.setId(reason.getId());
        data.setDetail(reason.getTitle());
        data.setRequiredDocs(reason.getRequiredDocs());

        return data;
    }
}
