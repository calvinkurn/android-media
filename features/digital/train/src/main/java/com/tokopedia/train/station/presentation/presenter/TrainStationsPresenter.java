package com.tokopedia.train.station.presentation.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.station.domain.TrainGetAllStationsUseCase;
import com.tokopedia.train.station.domain.TrainGetPopularStationsUseCase;
import com.tokopedia.train.station.domain.TrainGetStationCitiesByKeywordUseCase;
import com.tokopedia.train.station.domain.TrainGetStationsByKeywordUseCase;
import com.tokopedia.train.station.domain.model.TrainStation;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainAllStationsViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationGroupViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationsCityGroupViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.mapper.TrainStationViewModelMapper;
import com.tokopedia.train.station.presentation.contract.TrainStationsContract;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationsPresenter extends BaseDaggerPresenter<TrainStationsContract.View> implements TrainStationsContract.Presenter {

    private TrainGetStationsByKeywordUseCase trainGetStationsByKeywordUseCase;
    private TrainGetPopularStationsUseCase trainGetPopularStationsUseCase;
    private TrainGetStationCitiesByKeywordUseCase trainGetStationCitiesByKeywordUseCase;
    private TrainGetAllStationsUseCase trainGetAllStationsUseCase;
    private TrainStationViewModelMapper trainStationViewModelMapper;

    @Inject
    public TrainStationsPresenter(TrainGetStationsByKeywordUseCase trainGetStationsByKeywordUseCase,
                                  TrainGetPopularStationsUseCase trainGetPopularStationsUseCase,
                                  TrainGetStationCitiesByKeywordUseCase trainGetStationCitiesByKeywordUseCase,
                                  TrainGetAllStationsUseCase trainGetAllStationsUseCase, TrainStationViewModelMapper trainStationViewModelMapper) {
        this.trainGetStationsByKeywordUseCase = trainGetStationsByKeywordUseCase;
        this.trainGetPopularStationsUseCase = trainGetPopularStationsUseCase;
        this.trainGetStationCitiesByKeywordUseCase = trainGetStationCitiesByKeywordUseCase;
        this.trainGetAllStationsUseCase = trainGetAllStationsUseCase;
        this.trainStationViewModelMapper = trainStationViewModelMapper;
    }

    @Override
    public void actionOnInitialLoad() {
        trainGetPopularStationsUseCase.createObservable(trainGetPopularStationsUseCase.createRequest())
                .onErrorReturn(new Func1<Throwable, List<TrainStation>>() {
                    @Override
                    public List<TrainStation> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                }).zipWith(trainGetAllStationsUseCase.createObservable(RequestParams.create()), new Func2<List<TrainStation>, List<TrainStation>, List<Visitable>>() {
            @Override
            public List<Visitable> call(List<TrainStation> trainStations, List<TrainStation> allStations) {
                List<Visitable> visitables = new ArrayList<>();
                TrainPopularStationViewModel viewModel = new TrainPopularStationViewModel();
                viewModel.setStations(trainStationViewModelMapper.transform(trainStations));
                visitables.add(viewModel);
                visitables.add(new TrainAllStationsViewModel());
                visitables.addAll(trainStationViewModelMapper.transform(allStations));
                return visitables;
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Visitable>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onNext(List<Visitable> visitables) {
                getView().hideLoading();
                getView().renderStationList(visitables);
            }
        });
    }

    @Override
    public void onKeywordChange(String keyword) {
        getView().clearStationList();
        getView().showLoading();
        if (keyword.length() > 2) {
            trainGetStationsByKeywordUseCase.createObservable(trainGetStationsByKeywordUseCase.createRequest(keyword))
                    .onErrorReturn(new Func1<Throwable, List<TrainStation>>() {
                        @Override
                        public List<TrainStation> call(Throwable throwable) {
                            return new ArrayList<>();
                        }
                    })
                    .zipWith(
                            trainGetStationCitiesByKeywordUseCase.createObservable(trainGetStationCitiesByKeywordUseCase.createRequest(keyword))
                                    .onErrorReturn(new Func1<Throwable, List<TrainStation>>() {
                                        @Override
                                        public List<TrainStation> call(Throwable throwable) {
                                            return new ArrayList<>();
                                        }
                                    }),
                            new Func2<List<TrainStation>, List<TrainStation>, List<Visitable>>() {
                                @Override
                                public List<Visitable> call(List<TrainStation> stations, List<TrainStation> stationCities) {
                                    List<Visitable> visitables = new ArrayList<>();

                                    if (stationCities.size() > 0) {
                                        List<TrainStationViewModel> viewModels = trainStationViewModelMapper.transform(stationCities);
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
                            }
                    ).subscribe(new Subscriber<List<Visitable>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<Visitable> visitables) {
                    getView().hideLoading();
                    getView().renderStationList(visitables);
                }
            });
        } else if (keyword.length() == 0) {
            actionOnInitialLoad();
        }
    }
}
