package com.tokopedia.train.scheduledetail.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

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
                .flatMap((Func1<TrainScheduleViewModel, Observable<TrainScheduleDetailViewModel>>) trainScheduleViewModel -> {
                    if (trainScheduleViewModel != null) {
                        double totalAdultFare = numOfAdultPassenger * trainScheduleViewModel.getAdultFare();
                        double totalInfantFare = numOfInfantPassenger * trainScheduleViewModel.getInfantFare();
                        double totalPrice = totalAdultFare + totalInfantFare;
                        return Observable.zip(
                                trainRepository.getStationByStationCode(trainScheduleViewModel.getOrigin()),
                                trainRepository.getStationByStationCode(trainScheduleViewModel.getDestination()),
                                (origin, destination) -> new TrainScheduleDetailViewModel.Builder()
                                        .originCityName(origin.getCityName())
                                        .destinationCityName(destination.getCityName())
                                        .originStationName(origin.getStationName())
                                        .originStationCode(origin.getStationCode())
                                        .destinationStationName(destination.getStationName())
                                        .destinationStationCode(destination.getStationCode())
                                        .arrivalDate(TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                                                TrainDateUtil.FORMAT_DATE_LOCAL_DETAIL, trainScheduleViewModel.getArrivalTimestamp()))
                                        .arrivalTime(TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                                                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getArrivalTimestamp()))
                                        .departureDate(TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                                                TrainDateUtil.FORMAT_DATE_LOCAL_DETAIL, trainScheduleViewModel.getDepartureTimestamp()))
                                        .departureTime(TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                                                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getDepartureTimestamp()))
                                        .duration(trainScheduleViewModel.getDisplayDuration())
                                        .trainClass(trainScheduleViewModel.getDisplayClass())
                                        .subclass(trainScheduleViewModel.getSubclass())
                                        .trainName(trainScheduleViewModel.getTrainName())
                                        .trainNumber(trainScheduleViewModel.getTrainNumber())
                                        .displayAdultFare(trainScheduleViewModel.getDisplayAdultFare())
                                        .adultFare(trainScheduleViewModel.getAdultFare())
                                        .totalAdultFare(totalAdultFare)
                                        .displayInfantFare(trainScheduleViewModel.getDisplayInfantFare())
                                        .infantFare(trainScheduleViewModel.getInfantFare())
                                        .totalInfantFare(totalInfantFare)
                                        .totalPrice(totalPrice)
                                        .numOfAdultPassenger(numOfAdultPassenger)
                                        .numOfInfantPassenger(numOfInfantPassenger)
                                        .isReturnTrip(trainScheduleViewModel.isReturnTrip())
                                        .build());
                    } else {
                        return null;
                    }
                });
    }

    public RequestParams createRequestParams(String scheduleId, int numOfAdultPassenger, int numOfInfantPassenger) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_SCHEDULE_ID, scheduleId);
        requestParams.putInt(PARAM_NUMBER_OF_ADULT_PASSENGER, numOfAdultPassenger);
        requestParams.putInt(PARAM_NUMBER_OF_INFANT_PASSENGER, numOfInfantPassenger);
        return requestParams;
    }
}
