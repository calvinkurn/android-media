package com.tokopedia.train.station.presentation.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.station.domain.TrainGetAllStationsUseCase;
import com.tokopedia.train.station.domain.TrainGetPopularStationsUseCase;
import com.tokopedia.train.station.domain.TraingGetStationAutoCompleteUseCase;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainAllStationsViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainPopularStationViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.mapper.TrainStationViewModelMapper;
import com.tokopedia.train.station.presentation.contract.TrainStationsContract;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationsPresenter extends BaseDaggerPresenter<TrainStationsContract.View> implements TrainStationsContract.Presenter {
    private TrainGetPopularStationsUseCase trainGetPopularStationsUseCase;
    private TraingGetStationAutoCompleteUseCase traingGetStationAutoCompleteUseCase;
    private TrainGetAllStationsUseCase trainGetAllStationsUseCase;
    private TrainStationViewModelMapper trainStationViewModelMapper;
    private AutoCompleteInputListener inputListener;

    @Inject
    public TrainStationsPresenter(TrainGetPopularStationsUseCase trainGetPopularStationsUseCase,
                                  TraingGetStationAutoCompleteUseCase traingGetStationAutoCompleteUseCase,
                                  TrainGetAllStationsUseCase trainGetAllStationsUseCase,
                                  TrainStationViewModelMapper trainStationViewModelMapper) {
        this.trainGetPopularStationsUseCase = trainGetPopularStationsUseCase;
        this.traingGetStationAutoCompleteUseCase = traingGetStationAutoCompleteUseCase;
        this.trainGetAllStationsUseCase = trainGetAllStationsUseCase;
        this.trainStationViewModelMapper = trainStationViewModelMapper;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                inputListener = new AutoCompleteInputListener() {
                    @Override
                    public void onQuerySubmit(String query) {
                        subscriber.onNext(String.valueOf(query.trim()));
                    }
                };
            }
        }).debounce(250, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AutoCompleteKeywordSubscriber());
    }

    @Override
    public void actionOnInitialLoad() {
        getView().hideSearchView();
        trainGetPopularStationsUseCase.createObservable(trainGetPopularStationsUseCase.createRequest())
                .onErrorReturn(throwable -> new ArrayList<>())
                .zipWith(trainGetAllStationsUseCase.createObservable(RequestParams.create()), (trainStations, allStations) -> {
            List<Visitable> visitables = new ArrayList<>();
            TrainPopularStationViewModel viewModel = new TrainPopularStationViewModel();
            viewModel.setStations(trainStationViewModelMapper.transform(trainStations));
            visitables.add(viewModel);
            visitables.add(new TrainAllStationsViewModel());
            visitables.addAll(trainStationViewModelMapper.transform(allStations));
            return visitables;
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
                        if (isViewAttached()){
                            getView().showSearchView();
                            getView().hideLoading();
                        }
                    }

                    @Override
                    public void onNext(List<Visitable> visitables) {
                        getView().showSearchView();
                        getView().hideLoading();
                        getView().renderStationList(visitables);
                    }
                });
    }

    @Override
    public void onKeywordChange(String keyword) {
        if (inputListener != null) inputListener.onQuerySubmit(keyword);
    }

    @Override
    public void onViewCreated() {
        getView().renderSearchHint();
    }

    private void onKeywordReceived(String keyword) {
        getView().clearStationList();
        getView().showLoading();
        if (keyword.length() > 0) {
            traingGetStationAutoCompleteUseCase.execute(traingGetStationAutoCompleteUseCase.createRequest(keyword), new Subscriber<List<Visitable>>() {
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
        } else {
            actionOnInitialLoad();
        }
    }

    public interface AutoCompleteInputListener {
        void onQuerySubmit(String query);
    }

    private class AutoCompleteKeywordSubscriber extends Subscriber<String> {

        AutoCompleteKeywordSubscriber() {

        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String keyword) {
            if (!isUnsubscribed()) {
                onKeywordReceived(keyword);
            }
        }
    }

    @Override
    public void detachView() {
        trainGetAllStationsUseCase.unsubscribe();
        trainGetPopularStationsUseCase.unsubscribe();
        traingGetStationAutoCompleteUseCase.unsubscribe();
        super.detachView();
    }
}
