package com.tokopedia.train.search.domain;

import com.tokopedia.common.travel.constant.TravelSortOption;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.data.typedef.DepartureTimeTypeDef;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/22/18.
 */

public class GetFilterSearchParamDataUseCase extends UseCase<FilterSearchData> {

    private TrainRepository trainRepository;
    private FilterParam filterParam;
    private int scheduleVariant;

    public GetFilterSearchParamDataUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setFilterParam(FilterParam filterParam) {
        this.filterParam = filterParam;
    }

    public void setScheduleVariant(int scheduleVariant) {
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public Observable<FilterSearchData> createObservable(RequestParams requestParams) {
        return trainRepository.getFilteredAndSortedSchedule(filterParam, TravelSortOption.NO_PREFERENCE, scheduleVariant)
                .map(new Func1<List<TrainScheduleViewModel>, FilterSearchData>() {
                    @Override
                    public FilterSearchData call(List<TrainScheduleViewModel> trainScheduleViewModels) {
                        long minPrice = Integer.MIN_VALUE;
                        if (trainScheduleViewModels != null && trainScheduleViewModels.size() > 0){
                            minPrice = trainScheduleViewModels.get(0).getAdultFare();
                        }

                        long maxPrice = minPrice;
                        List<String> trainNameList = new ArrayList<>();
                        LinkedHashSet<String> trainNameSet = new LinkedHashSet<>();
                        List<String> trainClassList = new ArrayList<>();
                        LinkedHashSet<String> trainClassSet = new LinkedHashSet<>();
                        List<String> trainDepartureList = new ArrayList<>();

                        FilterSearchData filterSearchData = new FilterSearchData();
                        for (int i = 0; i < trainScheduleViewModels.size(); i++) {
                            if (minPrice > trainScheduleViewModels.get(i).getAdultFare()) {
                                minPrice = trainScheduleViewModels.get(i).getAdultFare();
                            }
                            if (maxPrice < trainScheduleViewModels.get(i).getAdultFare()) {
                                maxPrice = trainScheduleViewModels.get(i).getAdultFare();
                            }

                            trainNameSet.add(trainScheduleViewModels.get(i).getTrainName());
                            trainClassSet.add(trainScheduleViewModels.get(i).getDisplayClass());
                        }
                        filterSearchData.setMinPrice(minPrice);
                        filterSearchData.setMaxPrice(maxPrice);
                        trainNameList.addAll(trainNameSet);
                        filterSearchData.setTrains(trainNameList);
                        trainClassList.addAll(trainClassSet);
                        filterSearchData.setTrainClass(trainClassList);

                        trainDepartureList.add(DepartureTimeTypeDef.MORNING);
                        trainDepartureList.add(DepartureTimeTypeDef.AFTERNOON);
                        trainDepartureList.add(DepartureTimeTypeDef.NIGHT);
                        filterSearchData.setDepartureTimeList(trainDepartureList);

                        return filterSearchData;
                    }
                });
    }
}
