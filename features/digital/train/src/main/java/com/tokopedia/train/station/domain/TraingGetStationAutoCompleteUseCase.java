package com.tokopedia.train.station.domain;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationCityViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationGroupViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationsCityGroupViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.mapper.TrainStationCityViewModelMapper;
import com.tokopedia.train.station.presentation.adapter.viewmodel.mapper.TrainStationViewModelMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class TraingGetStationAutoCompleteUseCase extends UseCase<List<Visitable>> {
    private static final String PARAM_KEYWORD = "PARAM_KEYWORD";
    private TrainGetStationsByKeywordUseCase trainGetStationsByKeywordUseCase;
    private TrainGetStationCitiesByKeywordUseCase trainGetStationCitiesByKeywordUseCase;
    private TrainStationViewModelMapper trainStationViewModelMapper;
    private TrainStationCityViewModelMapper trainStationCityViewModelMapper;

    public TraingGetStationAutoCompleteUseCase(TrainGetStationsByKeywordUseCase trainGetStationsByKeywordUseCase,
                                               TrainGetStationCitiesByKeywordUseCase trainGetStationCitiesByKeywordUseCase,
                                               TrainStationViewModelMapper trainStationViewModelMapper,
                                               TrainStationCityViewModelMapper trainStationCityViewModelMapper) {
        this.trainGetStationsByKeywordUseCase = trainGetStationsByKeywordUseCase;
        this.trainGetStationCitiesByKeywordUseCase = trainGetStationCitiesByKeywordUseCase;
        this.trainStationViewModelMapper = trainStationViewModelMapper;
        this.trainStationCityViewModelMapper = trainStationCityViewModelMapper;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        String keyword = requestParams.getString(PARAM_KEYWORD, "");
        return trainGetStationsByKeywordUseCase.createObservable(trainGetStationsByKeywordUseCase.createRequest(keyword))
                .onErrorReturn(throwable -> new ArrayList<>())
                .zipWith(
                        trainGetStationCitiesByKeywordUseCase.createObservable(trainGetStationCitiesByKeywordUseCase.createRequest(keyword))
                                .onErrorReturn(throwable -> new ArrayList<>()),
                        (stations, stationCities) -> {
                            List<Visitable> visitables = new ArrayList<>();

                            if (stationCities.size() > 0) {
                                List<TrainStationCityViewModel> viewModels = trainStationCityViewModelMapper.transform(stationCities);
                                TrainStationsCityGroupViewModel cityGroupViewModel = new TrainStationsCityGroupViewModel();
                                cityGroupViewModel.setCities(new ArrayList<>());
                                visitables.add(cityGroupViewModel);
                                visitables.addAll(viewModels);
                            }

                            if (stations.size() > 0) {
                                List<TrainStationViewModel> viewModels = trainStationViewModelMapper.transform(stations);
                                TrainStationGroupViewModel stationGroupViewModel = new TrainStationGroupViewModel();
                                stationGroupViewModel.setStations(new ArrayList<>());
                                visitables.add(stationGroupViewModel);
                                visitables.addAll(viewModels);
                            }

                            return visitables;
                        }
                );
    }

    public RequestParams createRequest(String keyword) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_KEYWORD, keyword);
        return requestParams;
    }


}
