package com.tokopedia.train.search.domain;

import com.tokopedia.common.travel.constant.TravelSortOption;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author rizkyfadillah on 14/03/18.
 */

public class GetFilteredAndSortedScheduleUseCase extends UseCase<List<TrainScheduleViewModel>> {

    private static final String PARAM_FILTER = "PARAM_FILTER";
    private static final String PARAM_SORT_OPTION_ID = "PARAM_SORT_OPTION_ID";
    private static final String PARAM_IS_RETURN = "PARAM_IS_RETURN";

    private TrainRepository trainRepository;

    public GetFilteredAndSortedScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> createObservable(RequestParams requestParams) {
        FilterParam filterParam = (FilterParam) requestParams.getObject(PARAM_FILTER);
        int sortOptionId = requestParams.getInt(PARAM_SORT_OPTION_ID, TravelSortOption.NO_PREFERENCE);
        int scheduleVariant = requestParams.getInt(PARAM_IS_RETURN, 0);
        return trainRepository.getFilteredAndSortedSchedule(filterParam, sortOptionId, scheduleVariant);
    }

    public RequestParams createRequestParam(FilterParam filterParam, int sortOptionId, int scheduleVariant) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_FILTER, filterParam);
        requestParams.putInt(PARAM_SORT_OPTION_ID, sortOptionId);
        requestParams.putInt(PARAM_IS_RETURN, scheduleVariant);
        return requestParams;
    }

}
