package com.tokopedia.train.seat.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.seat.domain.TrainGetSeatsUseCase;
import com.tokopedia.train.seat.presentation.contract.TrainSeatContract;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class TrainSeatPresenter extends BaseDaggerPresenter<TrainSeatContract.View> implements TrainSeatContract.Presenter {
    private TrainGetSeatsUseCase trainGetSeatsUseCase;

    @Inject
    public TrainSeatPresenter(TrainGetSeatsUseCase trainGetSeatsUseCase) {
        this.trainGetSeatsUseCase = trainGetSeatsUseCase;
    }


    @Override
    public void getSeatMaps() {
        getView().showGetSeatMapLoading();
        getView().hidePage();
        trainGetSeatsUseCase.execute(RequestParams.create(), new Subscriber<List<TrainWagonViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()){
                    getView().showErrorGetSeatMaps(e.getMessage());
                    getView().hideGetSeatMapLoading();
                }
            }

            @Override
            public void onNext(List<TrainWagonViewModel> trainWagonViewModels) {
                getView().showPage();
                getView().hideGetSeatMapLoading();
                getView().renderWagon(trainWagonViewModels);
            }
        });
    }
}
