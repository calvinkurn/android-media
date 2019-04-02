package com.tokopedia.train.passenger.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class TrainSoftBookingUseCase extends UseCase<TrainSoftbook> {

    public static final String DEPARTURE_TRIP = "departureTrip";
    public static final String RETURN_TRIP = "returnTrip";
    public static final String BUYER = "buyer";
    public static final String PASSENGERS = "passengers";
    public static final String TOTAL_ADULT = "numPaxAdult";
    public static final String TOTAL_INFANT = "numPaxInfant";
    public static final String DEVICE = "device";
    public static final int DEFAULT_DEVICE = 5;

    private TrainRepository trainRepository;

    public TrainSoftBookingUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainSoftbook> createObservable(RequestParams requestParams) {
        return trainRepository.doSoftBookTrainTicket(requestParams.getParameters());
    }
}