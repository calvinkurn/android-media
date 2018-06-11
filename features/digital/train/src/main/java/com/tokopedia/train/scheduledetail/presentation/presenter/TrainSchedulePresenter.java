package com.tokopedia.train.scheduledetail.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.scheduledetail.domain.GetScheduleDetailUseCase;
import com.tokopedia.train.scheduledetail.presentation.contract.TrainScheduleContract;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 07/06/18.
 */
public class TrainSchedulePresenter extends BaseDaggerPresenter<TrainScheduleContract.View> implements
        TrainScheduleContract.Presenter {

    private GetScheduleDetailUseCase getScheduleDetailUseCase;

    @Inject
    public TrainSchedulePresenter(GetScheduleDetailUseCase getScheduleDetailUseCase) {
        this.getScheduleDetailUseCase = getScheduleDetailUseCase;
    }

    @Override
    public void getScheduleDetail(String scheduleId) {
        getScheduleDetailUseCase.execute(
                getScheduleDetailUseCase.createRequestParams(scheduleId),
                new Subscriber<TrainScheduleDetailViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
                        getView().showScheduleDetail(trainScheduleDetailViewModel);
                    }
                });
    }

}
