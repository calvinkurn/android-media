package com.tokopedia.train.seat.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.seat.data.entity.TrainChangeSeatEntity;
import com.tokopedia.train.seat.domain.model.TrainPassengerSeat;
import com.tokopedia.train.seat.domain.model.TrainPassengerSeatMapper;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class TrainChangeSeatUseCase extends UseCase<List<TrainPassengerSeat>> {
    private static final String EXTRA_CHANGES = "EXTRA_CHANGES";
    private TrainRepository trainRepository;
    private TrainPassengerSeatMapper trainPassengerSeatMapper;

    public TrainChangeSeatUseCase(TrainRepository trainRepository, TrainPassengerSeatMapper trainPassengerSeatMapper) {
        this.trainRepository = trainRepository;
        this.trainPassengerSeatMapper = trainPassengerSeatMapper;
    }

    @Override
    public Observable<List<TrainPassengerSeat>> createObservable(RequestParams requestParams) {
        List<ChangeSeatMapRequest> requests = (List<ChangeSeatMapRequest>) requestParams.getObject(EXTRA_CHANGES);
        return trainRepository
                .changeSeats(requests)
                .map(new Func1<List<TrainChangeSeatEntity>, List<TrainPassengerSeat>>() {
                    @Override
                    public List<TrainPassengerSeat> call(List<TrainChangeSeatEntity> trainChangeSeatEntities) {
                        return trainPassengerSeatMapper.transform(trainChangeSeatEntities);
                    }
                });
    }

    public RequestParams createRequest(List<ChangeSeatMapRequest> requests) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(EXTRA_CHANGES, requests);
        return requestParams;
    }
}
