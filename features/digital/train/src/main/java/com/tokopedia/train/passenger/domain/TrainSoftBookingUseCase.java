package com.tokopedia.train.passenger.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.passenger.domain.model.TrainPaxPassenger;
import com.tokopedia.train.passenger.domain.model.TrainSeat;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.domain.model.TrainTrip;
import com.tokopedia.train.passenger.domain.model.TrainTripBuilder;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class TrainSoftBookingUseCase extends UseCase<TrainSoftbook> {

    private TrainRepository trainRepository;

    public TrainSoftBookingUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainSoftbook> createObservable(RequestParams requestParams) {
        TrainSoftbook trainSoftbook = TrainSoftbook.dummy();

        return Observable.just(trainSoftbook);
    }

    public RequestParams create() {
        return RequestParams.create();
    }

}