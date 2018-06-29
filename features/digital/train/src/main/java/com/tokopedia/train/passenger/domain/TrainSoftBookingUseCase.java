package com.tokopedia.train.passenger.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class TrainSoftBookingUseCase extends UseCase<TrainSoftbook> {
    private TrainRepository trainRepository;

    public TrainSoftBookingUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainSoftbook> createObservable(RequestParams requestParams) {
        return Observable.empty();
    }

    public RequestParams create() {
        return RequestParams.create();
    }
}
