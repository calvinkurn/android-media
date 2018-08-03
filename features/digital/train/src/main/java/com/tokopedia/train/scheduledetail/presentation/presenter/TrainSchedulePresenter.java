package com.tokopedia.train.scheduledetail.presentation.presenter;

import android.util.Pair;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.scheduledetail.domain.GetScheduleDetailUseCase;
import com.tokopedia.train.scheduledetail.presentation.contract.TrainScheduleContract;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Rizky on 07/06/18.
 */
public class TrainSchedulePresenter extends BaseDaggerPresenter<TrainScheduleContract.View> implements
        TrainScheduleContract.Presenter {

    private GetScheduleDetailUseCase getScheduleDetailUseCase;
    private GetDetailScheduleUseCase getDetailScheduleUseCase;

    @Inject
    public TrainSchedulePresenter(GetScheduleDetailUseCase getScheduleDetailUseCase,
                                  GetDetailScheduleUseCase getDetailScheduleUseCase) {
        this.getScheduleDetailUseCase = getScheduleDetailUseCase;
        this.getDetailScheduleUseCase = getDetailScheduleUseCase;
    }

    @Override
    public void getScheduleDetail(String scheduleId, int numOfAdultPassenger, int numOfInfantPassenger) {
        getView().showLoading();

        getDetailScheduleUseCase.setIdSchedule(scheduleId);
        getDetailScheduleUseCase.createObservable(RequestParams.EMPTY)
                .zipWith(getScheduleDetailUseCase.createObservable(
                        getScheduleDetailUseCase.createRequestParams(scheduleId, numOfAdultPassenger, numOfInfantPassenger)),
                        Pair::create)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Pair<TrainScheduleViewModel, TrainScheduleDetailViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Pair<TrainScheduleViewModel, TrainScheduleDetailViewModel> modelPair) {
                        getView().stopLoading();
                        getView().showScheduleDetail(modelPair.first, modelPair.second);
                    }
                });
    }

    @Override
    public void detachView() {
        getDetailScheduleUseCase.unsubscribe();
        getScheduleDetailUseCase.unsubscribe();
        super.detachView();
    }
}
