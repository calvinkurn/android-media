package com.tokopedia.train.scheduledetail.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.common.util.TrainDateUtil;
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
    private static final String PARAM_NUMBER_OF_ADULT_PASSENGER = "PARAM_NUMBER_OF_ADULT_PASSENGER";
    private static final String PARAM_NUMBER_OF_INFANT_PASSENGER = "PARAM_NUMBER_OF_INFANT_PASSENGER";

    private TrainRepository trainRepository;

    @Inject
    public GetScheduleDetailUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<TrainScheduleDetailViewModel> createObservable(RequestParams requestParams) {
        String scheduleId = requestParams.getString(PARAM_SCHEDULE_ID, "");
        int numOfAdultPassenger = requestParams.getInt(PARAM_NUMBER_OF_ADULT_PASSENGER, 0);
        int numOfInfantPassenger = requestParams.getInt(PARAM_NUMBER_OF_INFANT_PASSENGER, 0);

        return trainRepository.getDetailSchedule(scheduleId)
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
                                .arrivalDate(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                                        TrainDateUtil.DEFAULT_VIEW_LOCAL_DETAIL, trainScheduleViewModel.getArrivalTimestamp()))
                                .arrivalTime(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                                        TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getArrivalTimestamp()))
                                .departureDate(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                                        TrainDateUtil.DEFAULT_VIEW_LOCAL_DETAIL, trainScheduleViewModel.getDepartureTimestamp()))
                                .departureTime(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                                        TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getDepartureTimestamp()))
                                .duration(trainScheduleViewModel.getDisplayDuration())
                                .trainClass(trainScheduleViewModel.getDisplayClass())
                                .trainName(trainScheduleViewModel.getTrainName())
                                .displayAdultFare(trainScheduleViewModel.getDisplayAdultFare())
                                .adultFare(trainScheduleViewModel.getAdultFare())
                                .totalAdultFare(numOfAdultPassenger * trainScheduleViewModel.getAdultFare())
                                .displayInfantFare(trainScheduleViewModel.getDisplayInfantFare())
                                .infantFare(trainScheduleViewModel.getInfantFare())
                                .totalInfantFare(numOfInfantPassenger * trainScheduleViewModel.getInfantFare())
                                .numOfAdultPassenger(numOfAdultPassenger)
                                .numOfInfantPassenger(numOfInfantPassenger)
                                .build()));
    }

    public RequestParams createRequestParams(String scheduleId, int numOfAdultPassenger, int numOfInfantPassenger) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_SCHEDULE_ID, scheduleId);
        requestParams.putInt(PARAM_NUMBER_OF_ADULT_PASSENGER, numOfAdultPassenger);
        requestParams.putInt(PARAM_NUMBER_OF_INFANT_PASSENGER, numOfInfantPassenger);
        return requestParams;
    }
}
