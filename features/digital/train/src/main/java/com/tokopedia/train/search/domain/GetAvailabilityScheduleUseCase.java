package com.tokopedia.train.search.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

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
                .flatMap(new Func1<AvailabilityKeySchedule, Observable<List<TrainScheduleViewModel>>>() {
                    @Override
                    public Observable<List<TrainScheduleViewModel>> call(AvailabilityKeySchedule availabilityKeySchedule) {
                        RequestParams requestParams = RequestParams.create();
                        requestParams.putObject(TRAIN_ID_KEY, availabilityKeySchedule.getIdTrain());
                        return Observable.zip(Observable.just(availabilityKeySchedule),
                                trainRepository.getAvailabilitySchedule(requestParams.getParameters()),
                                new Func2<AvailabilityKeySchedule, List<TrainScheduleViewModel>, List<TrainScheduleViewModel>>() {
                                    @Override
                                    public List<TrainScheduleViewModel> call(AvailabilityKeySchedule availabilityKeySchedule, List<TrainScheduleViewModel> trainScheduleViewModels) {
                                        return trainScheduleViewModels;
                                    }
                                });
                    }
                })
                .toList();
    }
}
