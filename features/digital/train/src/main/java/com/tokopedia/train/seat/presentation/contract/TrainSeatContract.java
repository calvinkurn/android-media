package com.tokopedia.train.seat.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface TrainSeatContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
