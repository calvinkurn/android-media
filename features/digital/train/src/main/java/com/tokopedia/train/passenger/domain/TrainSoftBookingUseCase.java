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
        List<TrainTrip> departureTrainTrips = new ArrayList<>();
        List<TrainPaxPassenger> departurePaxPassengers = new ArrayList<>();

        TrainSeat departureTrainSeat = new TrainSeat("Ekonomi", "4", "4", "D");
        departurePaxPassengers.add(new TrainPaxPassenger("Rizky Fadillah", "03910920", 1, departureTrainSeat));
        TrainTrip departureTrainTrip = new TrainTripBuilder()
                .paxPassengers(departurePaxPassengers)
                .createTrainTrip();
        departureTrainTrips.add(departureTrainTrip);

        TrainSeat departureTrainSeat2 = new TrainSeat("Ekonomi", "4", "4", "E");
        departurePaxPassengers.add(new TrainPaxPassenger("Sarah Nurlaila", "23456263", 1, departureTrainSeat2));
        TrainTrip departureTrainTrip2 = new TrainTripBuilder()
                .paxPassengers(departurePaxPassengers)
                .createTrainTrip();
        departureTrainTrips.add(departureTrainTrip2);

        TrainSoftbook trainSoftbook = new TrainSoftbook(null, null,  null,
                departureTrainTrips, null);

        return Observable.just(trainSoftbook);
    }

    public RequestParams create() {
        return RequestParams.create();
    }

}