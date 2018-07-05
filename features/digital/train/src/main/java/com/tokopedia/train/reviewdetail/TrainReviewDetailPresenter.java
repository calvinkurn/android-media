package com.tokopedia.train.reviewdetail;

import android.text.TextUtils;
import android.util.Pair;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.passenger.domain.model.TrainPaxPassenger;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailPresenter extends BaseDaggerPresenter<TrainReviewDetailContract.View>
        implements TrainReviewDetailContract.Presenter {

    private GetDetailScheduleUseCase getDetailScheduleUseCase;

    @Inject
    public TrainReviewDetailPresenter(GetDetailScheduleUseCase getDetailScheduleUseCase) {
        this.getDetailScheduleUseCase = getDetailScheduleUseCase;
    }

    @Override
    public void getPassengers(TrainSoftbook trainSoftbook) {
        Observable<TrainPaxPassenger> departureTrainPaxPassengerObservable =
                Observable.just(trainSoftbook.getDepartureTrips().get(0).getPaxPassengers())
                        .flatMapIterable((Func1<List<TrainPaxPassenger>, Iterable<TrainPaxPassenger>>)
                                trainPaxPassengers -> trainPaxPassengers);

        Observable<TrainPaxPassenger> returnTrainPaxPassengerObservable = null;
        if (trainSoftbook.getReturnTrips() != null && !trainSoftbook.getReturnTrips().isEmpty()) {
            returnTrainPaxPassengerObservable =
                    Observable.just(trainSoftbook.getReturnTrips().get(0).getPaxPassengers())
                            .flatMapIterable((Func1<List<TrainPaxPassenger>, Iterable<TrainPaxPassenger>>)
                                    trainPaxPassengers -> trainPaxPassengers);
        }

        Observable<List<TrainReviewPassengerInfoViewModel>> observable;

        if (returnTrainPaxPassengerObservable != null) {
            observable = Observable.zip(departureTrainPaxPassengerObservable, returnTrainPaxPassengerObservable,
                    (departureTrainPaxPassenger, returnTrainPaxPassenger)
                            -> new TrainReviewPassengerInfoViewModelBuilder()
                            .name(departureTrainPaxPassenger.getName())
                            .noID(departureTrainPaxPassenger.getIdNumber())
                            .departureTripClass(departureTrainPaxPassenger.getSeat().getKlass())
                            .returnTripClass(returnTrainPaxPassenger.getSeat().getKlass())
                            .departureSeat(departureTrainPaxPassenger.getSeat().getWagonNo() + "/"
                                    + departureTrainPaxPassenger.getSeat().getRow()
                                    + departureTrainPaxPassenger.getSeat().getColumn())
                            .returnSeat(returnTrainPaxPassenger.getSeat().getWagonNo() + "/"
                                    + returnTrainPaxPassenger.getSeat().getRow()
                                    + returnTrainPaxPassenger.getSeat().getColumn())
                            .createTrainReviewPassengerInfoViewModel())
                    .toList();
        } else {
            observable = departureTrainPaxPassengerObservable
                    .map(departureTrainPaxPassenger -> new TrainReviewPassengerInfoViewModelBuilder()
                            .name(departureTrainPaxPassenger.getName())
                            .noID(departureTrainPaxPassenger.getIdNumber())
                            .departureTripClass(departureTrainPaxPassenger.getSeat().getKlass())
                            .returnTripClass(null)
                            .departureSeat(departureTrainPaxPassenger.getSeat().getWagonNo() + "/"
                                    + departureTrainPaxPassenger.getSeat().getRow()
                                    + departureTrainPaxPassenger.getSeat().getColumn())
                            .returnSeat(null)
                            .createTrainReviewPassengerInfoViewModel())
                    .toList();
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TrainReviewPassengerInfoViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<TrainReviewPassengerInfoViewModel> trainReviewPassengerInfoViewModels) {
                        getView().renderList(trainReviewPassengerInfoViewModels);
                    }
                });
    }

    @Override
    public void getScheduleDetail(String departureScheduleId, String returnScheduleId) {
        getDetailScheduleUseCase.setIdSchedule(departureScheduleId);
        Observable<TrainScheduleViewModel> departureSchedule = getDetailScheduleUseCase.createObservable(RequestParams.EMPTY);

        getDetailScheduleUseCase.setIdSchedule(returnScheduleId);
        Observable<TrainScheduleViewModel> returnSchedule = getDetailScheduleUseCase.createObservable(RequestParams.EMPTY);

        departureSchedule.zipWith(returnSchedule, Pair::create)
                .subscribe(new Subscriber<Pair<TrainScheduleViewModel, TrainScheduleViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Pair<TrainScheduleViewModel, TrainScheduleViewModel> pairScheduleDetail) {
                        getView().showScheduleTrips(pairScheduleDetail.first, pairScheduleDetail.second);
                    }
                });
    }

}