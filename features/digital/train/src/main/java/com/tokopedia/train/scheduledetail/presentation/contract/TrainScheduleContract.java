package com.tokopedia.train.scheduledetail.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by Rizky on 07/06/18.
 */
public interface TrainScheduleContract {

    interface View extends CustomerView {

        void showScheduleDetail(TrainScheduleViewModel trainScheduleViewModel, TrainScheduleDetailViewModel trainScheduleDetailViewModel);

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getScheduleDetail(String scheduleId, int numOfAdultPassenger, int numOfInfantPassenger);

    }

}
