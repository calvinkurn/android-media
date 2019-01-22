package com.tokopedia.train.reviewdetail.presentation.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.checkout.domain.TrainCheckoutUseCase;
import com.tokopedia.train.checkout.presentation.model.TrainCheckoutViewModel;
import com.tokopedia.train.common.data.interceptor.TrainNetworkException;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.passenger.data.TrainBookingPassenger;
import com.tokopedia.train.passenger.domain.model.TrainPaxPassenger;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.presentation.contract.TrainReviewDetailContract;
import com.tokopedia.train.reviewdetail.presentation.model.TrainReviewPassengerInfoViewModel;
import com.tokopedia.train.reviewdetail.presentation.model.TrainReviewPassengerInfoViewModelBuilder;
import com.tokopedia.train.scheduledetail.domain.GetScheduleDetailUseCase;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailPresenter extends BaseDaggerPresenter<TrainReviewDetailContract.View>
        implements TrainReviewDetailContract.Presenter {

    private static final String TAG = TrainReviewDetailPresenter.class.getSimpleName();

    private GetDetailScheduleUseCase getDetailScheduleUseCase;
    private GetScheduleDetailUseCase getScheduleDetailUseCase;
    private TrainCheckoutUseCase trainCheckoutUseCase;

    @Inject
    public TrainReviewDetailPresenter(GetDetailScheduleUseCase getDetailScheduleUseCase,
                                      GetScheduleDetailUseCase getScheduleDetailUseCase,
                                      TrainCheckoutUseCase trainCheckoutUseCase) {
        this.getDetailScheduleUseCase = getDetailScheduleUseCase;
        this.getScheduleDetailUseCase = getScheduleDetailUseCase;
        this.trainCheckoutUseCase = trainCheckoutUseCase;
    }

    @Override
    public void getPassengers(TrainSoftbook trainSoftbook, String originStationCode,
                              String destinationStationCode) {
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
                            -> {
                        String passengerType = null;
                        if (departureTrainPaxPassenger.getPaxType() == TrainBookingPassenger.ADULT) {
                            passengerType = getView().getPassengerTypeAdult();
                        } else if (departureTrainPaxPassenger.getPaxType() == TrainBookingPassenger.INFANT) {
                            passengerType = getView().getPassengerTypeChild();
                        }
                        return new TrainReviewPassengerInfoViewModelBuilder()
                                .name(departureTrainPaxPassenger.getName())
                                .noID(departureTrainPaxPassenger.getIdNumber())
                                .originStationCode(originStationCode)
                                .destinationStationCode(destinationStationCode)
                                .departureSeat(departureTrainPaxPassenger.getSeat().getWagonNo() + "/"
                                        + departureTrainPaxPassenger.getSeat().getRow()
                                        + departureTrainPaxPassenger.getSeat().getColumn())
                                .returnSeat(returnTrainPaxPassenger.getSeat().getWagonNo() + "/"
                                        + returnTrainPaxPassenger.getSeat().getRow()
                                        + returnTrainPaxPassenger.getSeat().getColumn())
                                .passengerTypeStr(passengerType)
                                .passengerType(departureTrainPaxPassenger.getPaxType())
                                .createTrainReviewPassengerInfoViewModel();
                    })
                    .toList();
        } else {
            observable = departureTrainPaxPassengerObservable
                    .map(departureTrainPaxPassenger -> {
                        String passengerType = null;
                        if (departureTrainPaxPassenger.getPaxType() == TrainBookingPassenger.ADULT) {
                            passengerType = getView().getPassengerTypeAdult();
                        } else if (departureTrainPaxPassenger.getPaxType() == TrainBookingPassenger.INFANT) {
                            passengerType = getView().getPassengerTypeChild();
                        }
                        return new TrainReviewPassengerInfoViewModelBuilder()
                                .name(departureTrainPaxPassenger.getName())
                                .noID(departureTrainPaxPassenger.getIdNumber())
                                .returnTripClass(null)
                                .originStationCode(originStationCode)
                                .destinationStationCode(destinationStationCode)
                                .departureSeat(departureTrainPaxPassenger.getSeat().getWagonNo() + "/"
                                        + departureTrainPaxPassenger.getSeat().getRow()
                                        + departureTrainPaxPassenger.getSeat().getColumn())
                                .returnSeat(null)
                                .passengerTypeStr(passengerType)
                                .passengerType(departureTrainPaxPassenger.getPaxType())
                                .createTrainReviewPassengerInfoViewModel();
                    })
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
    public void getScheduleTrips(String departureScheduleId, String returnScheduleId) {
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
                        getView().showDepartureTrip(pairScheduleDetail.first);
                        if (pairScheduleDetail.second != null) {
                            getView().showReturnTrip(pairScheduleDetail.second);
                        } else {
                            getView().hideReturnTrip();
                        }
                    }
                });
    }

    @Override
    public void getScheduleTripsPrice(String departureScheduleId, String returnScheduleId, int numOfAdultPassenger,
                                      int numOfInfantPassenger) {
        Observable<TrainScheduleDetailViewModel> departureSchedule = getScheduleDetailUseCase.createObservable(
                getScheduleDetailUseCase.createRequestParams(departureScheduleId, numOfAdultPassenger, numOfInfantPassenger)
        );

        Observable<TrainScheduleDetailViewModel> returnSchedule = null;
        if (!TextUtils.isEmpty(returnScheduleId)) {
            returnSchedule = getScheduleDetailUseCase.createObservable(
                    getScheduleDetailUseCase.createRequestParams(returnScheduleId, numOfAdultPassenger, numOfInfantPassenger)
            );
        }

        Observable<Pair<TrainScheduleDetailViewModel, TrainScheduleDetailViewModel>> finalObservable;

        if (returnSchedule != null) {
            finalObservable = departureSchedule.zipWith(returnSchedule, Pair::create);
        } else {
            finalObservable = departureSchedule.map(trainScheduleDetailViewModel ->
                    Pair.create(trainScheduleDetailViewModel, null));
        }

        finalObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Pair<TrainScheduleDetailViewModel, TrainScheduleDetailViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Pair<TrainScheduleDetailViewModel, TrainScheduleDetailViewModel> pairScheduleDetail) {
                        getView().showScheduleTripsPrice(pairScheduleDetail.first, pairScheduleDetail.second);
                        getView().startCountdown(TrainDateUtil.stringToDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                                getView().getExpireDate()));
                    }
                });
    }

    @Override
    public void checkout(String reservationId, String reservationCode, String galaCode, String client, String version) {
        getView().showCheckoutLoading();

        trainCheckoutUseCase.execute(
                trainCheckoutUseCase.createRequestParams(reservationId, reservationCode, galaCode, client, version),
                new Subscriber<TrainCheckoutViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof TrainNetworkException) {
                            getView().showCheckoutFailed(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(TrainCheckoutViewModel trainCheckoutViewModel) {
                        getView().navigateToTopPayActivity(trainCheckoutViewModel);
                    }
                }
        );
    }

    @Override
    public void onPaymentSuccess() {
        getView().navigateToOrderList();
    }

    @Override
    public void onPaymentFailed() {
        getView().showPaymentFailedErrorMessage(R.string.train_review_failed_checkout_message);
    }

    @Override
    public void onPaymentCancelled() {
        getView().setNeedToRefreshOnPassengerInfo();
        getView().showPaymentFailedErrorMessage(R.string.train_review_cancel_checkout_message);
    }

    @Override
    public void onRunningOutOfTime() {
        if (isViewAttached()) {
            getView().showExpiredPaymentDialog();
        }
    }

    @Override
    public void detachView() {
        getDetailScheduleUseCase.unsubscribe();
        getScheduleDetailUseCase.unsubscribe();
        trainCheckoutUseCase.unsubscribe();
        super.detachView();
    }
}