package com.tokopedia.train.search.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class GetAvailabilityScheduleUseCase extends UseCase<List<List<TrainScheduleViewModel>>> {

    public static final String TRAIN_ID_KEY = "avbKeys";

    private TrainRepository trainRepository;

    private List<AvailabilityKeySchedule> availabilityKeySchedules;

    public GetAvailabilityScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setAvailabilityKeySchedules(List<AvailabilityKeySchedule> availabilityKeySchedules) {
        this.availabilityKeySchedules = availabilityKeySchedules;
    }

    @Override
    public Observable<List<List<TrainScheduleViewModel>>> createObservable(RequestParams requestParams) {
        return Observable.from(availabilityKeySchedules)
                .flatMap(it -> {
                    RequestParams reqParam = RequestParams.create();
                    reqParam.putObject(TRAIN_ID_KEY, it.getIdTrain());
                    return trainRepository.getAvailabilitySchedule(reqParam.getParameters());
                })
                .toList();
    }
}
