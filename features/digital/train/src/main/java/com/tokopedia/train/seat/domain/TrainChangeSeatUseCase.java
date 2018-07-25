package com.tokopedia.train.seat.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.seat.domain.model.TrainPassengerSeat;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

public class TrainChangeSeatUseCase extends UseCase<List<TrainPassengerSeat>> {
    private static final String EXTRA_CHANGES = "EXTRA_CHANGES";
    private TrainRepository trainRepository;

    public TrainChangeSeatUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<TrainPassengerSeat>> createObservable(RequestParams requestParams) {
        List<ChangeSeatMapRequest> requests = (List<ChangeSeatMapRequest>) requestParams.getObject(EXTRA_CHANGES);
        return trainRepository.changeSeats(requests);
    }

    public RequestParams createRequest(List<ChangeSeatMapRequest> requests) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(EXTRA_CHANGES, requests);
        return requestParams;
    }
}
