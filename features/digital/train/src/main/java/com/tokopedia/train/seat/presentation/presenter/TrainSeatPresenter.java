package com.tokopedia.train.seat.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.train.seat.presentation.contract.TrainSeatContract;

import javax.inject.Inject;

public class TrainSeatPresenter extends BaseDaggerPresenter<TrainSeatContract.View> implements TrainSeatContract.Presenter {
    @Inject
    public TrainSeatPresenter() {
    }
}
