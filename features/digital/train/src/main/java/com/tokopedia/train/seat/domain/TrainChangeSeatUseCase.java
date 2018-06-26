package com.tokopedia.train.seat.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.seat.domain.model.TrainPassengerSeat;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

public class TrainChangeSeatUseCase extends UseCase<List<TrainPassengerSeat>> {
    private TrainRepository trainRepository;

    public TrainChangeSeatUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<TrainPassengerSeat>> createObservable(RequestParams requestParams) {
        return null;
    }
}
