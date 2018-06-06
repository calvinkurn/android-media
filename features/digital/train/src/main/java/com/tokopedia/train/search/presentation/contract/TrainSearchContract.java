package com.tokopedia.train.search.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/13/18.
 */

public interface TrainSearchContract {

    interface View extends BaseListViewListener<TrainScheduleViewModel> {
        RequestParams getRequestParam();

        void hideLayoutTripInfo();

        void showLayoutTripInfo();

        void showDataFromCache(List<TrainScheduleViewModel> trainScheduleViewModels);

        void setSortOptionId(int sortOptionId);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getTrainSchedules(int scheduleVariant);

        void getFilteredAndSortedSchedules(long minPrice, long maxPrice, List<String> trainClass,
                                           List<String> trains, List<String> departureTrains, int sortOptionId);
    }
}
