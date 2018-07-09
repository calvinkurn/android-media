package com.tokopedia.train.search.domain;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/21/18.
 */

public class GetTotalScheduleUseCase extends UseCase<Integer> {

    private static final String FILTER_PARAM = "filter_param";

    private TrainRepository trainRepository;

    private String arrivalTimestampSelected;

    private int scheduleVariant;

    public GetTotalScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setArrivalTimestampSelected(String arrivalTimestampSelected) {
        this.arrivalTimestampSelected = arrivalTimestampSelected;
    }

    public void setScheduleVariant(int scheduleVariant) {
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public Observable<Integer> createObservable(RequestParams requestParams) {
        return trainRepository.getCountSchedule(
                (FilterSearchData) requestParams.getObject(FILTER_PARAM),
                scheduleVariant, arrivalTimestampSelected);
    }

    public RequestParams createRequestParam(FilterSearchData filterSearchData) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(FILTER_PARAM, filterSearchData);
        return requestParams;
    }
}
