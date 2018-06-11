package com.tokopedia.train.scheduledetail.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.station.domain.model.TrainStation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Rizky on 07/06/18.
 */
public class GetScheduleDetailUseCase extends UseCase<TrainScheduleDetailViewModel> {

    private static final String PARAM_SCHEDULE_ID = "PARAM_SCHEDULE_ID";

    private TrainRepository trainRepository;

    @Inject
    public GetScheduleDetailUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainScheduleDetailViewModel> createObservable(RequestParams requestParams) {
        return trainRepository.getDetailSchedule(requestParams.getString(PARAM_SCHEDULE_ID, ""))
                .flatMap((Func1<TrainScheduleViewModel, Observable<TrainScheduleDetailViewModel>>) trainScheduleViewModel -> Observable.zip(
                        trainRepository.getStationByStationCode(trainScheduleViewModel.getOrigin()),
                        trainRepository.getStationByStationCode(trainScheduleViewModel.getDestination()),
                        (origin, destination) -> new TrainScheduleDetailViewModel.Builder()
                                .originCityName(origin.getCityName())
                                .destinationCityName(destination.getCityName())
                                .originStationName(origin.getStationName())
                                .originStationCode(origin.getStationCode())
                                .destinationStationName(destination.getStationName())
                                .destinationStationCode(destination.getStationCode())
                                .arrivalDate(trainScheduleViewModel.getArrivalTimestamp())
                                .departureDate(trainScheduleViewModel.getDepartureTimestamp())
                                .duration(trainScheduleViewModel.getDisplayDuration())
                                .trainClass(trainScheduleViewModel.getDisplayClass())
                                .trainName(trainScheduleViewModel.getTrainName())
                                .build()));
    }

    public RequestParams createRequestParams(String scheduleId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_SCHEDULE_ID, scheduleId);
        return requestParams;
    }
}
