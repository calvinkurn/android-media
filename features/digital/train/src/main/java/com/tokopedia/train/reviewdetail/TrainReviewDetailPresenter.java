package com.tokopedia.train.reviewdetail;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.passenger.domain.model.TrainPaxPassenger;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailPresenter extends BaseDaggerPresenter<TrainReviewDetailContract.View>
        implements TrainReviewDetailContract.Presenter {

    @Override
    public void getPassengers(TrainSoftbook trainSoftbook) {
//        List<TrainSeatPassengerSeatViewModel> trainSeatPassengerSeatViewModels = new ArrayList<>();
//
//        Observable<List<TrainPaxPassenger>> departureTrainPaxPassengers =
//                Observable.just(trainSoftbook.getDepartureTrips().get(0).getPaxPassengers());
//
//        Observable<List<TrainPaxPassenger>> returnTrainPaxPassengers =
//                Observable.just(trainSoftbook.getReturnTrips().get(0).getPaxPassengers());
//
//        Observable.zip(departureTrainPaxPassengers, returnTrainPaxPassengers, new Func2<List<TrainPaxPassenger>, List<TrainPaxPassenger>, Object>() {
//            @Override
//            public List<TrainSeatPassengerSeatViewModel> call(List<TrainPaxPassenger> departureTrainPaxPassengers, List<TrainPaxPassenger> returnTrainPaxPassengers) {
//                return null;
//            }
//        });
//
//        for (TrainPaxPassenger trainPaxPassenger : trainSoftbook.getDepartureTrips().get(0).getPaxPassengers()) {
//            trainSeatPassengerSeatViewModels.add(new TrainSeatPassengerSeatViewModel(
//                    trainPaxPassenger.getSeat().getWagonNo(),
//                    trainPaxPassenger.getSeat().getKlass(),
//                    trainPaxPassenger.getSeat().getRow(),
//                    trainPaxPassenger.getSeat().getColumn()
//            ));
//        }

        Observable<TrainPaxPassenger> departureTrainPaxPassenger =
                Observable.just(trainSoftbook.getDepartureTrips().get(0).getPaxPassengers())
                .flatMapIterable((Func1<List<TrainPaxPassenger>, Iterable<TrainPaxPassenger>>)
                        trainPaxPassengers -> trainPaxPassengers);

        Observable<TrainPaxPassenger> returnTrainPaxPassenger =
                Observable.just(trainSoftbook.getReturnTrips().get(0).getPaxPassengers())
                .flatMapIterable((Func1<List<TrainPaxPassenger>, Iterable<TrainPaxPassenger>>)
                        trainPaxPassengers -> trainPaxPassengers);

        Observable.zip(departureTrainPaxPassenger, returnTrainPaxPassenger, (departureTrainPaxPassenger1, returnTrainPaxPassenger1)
                -> new TrainReviewPassengerInfoViewModel(
                departureTrainPaxPassenger1.getName(), departureTrainPaxPassenger1.getIdNumber(),
                departureTrainPaxPassenger1.getSeat().getKlass(), returnTrainPaxPassenger1.getSeat().getKlass(),
                departureTrainPaxPassenger1.getSeat().getWagonNo() + "/"
                        + departureTrainPaxPassenger1.getSeat().getRow()
                        + departureTrainPaxPassenger1.getSeat().getColumn(),
                returnTrainPaxPassenger1.getSeat().getWagonNo() + "/"
                        + returnTrainPaxPassenger1.getSeat().getRow()
                        + returnTrainPaxPassenger1.getSeat().getColumn())).toList().subscribe(new Subscriber<List<TrainReviewPassengerInfoViewModel>>() {
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

}