package com.tokopedia.train.search.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/13/18.
 */

public interface TrainSearchContract {

    interface View extends BaseListViewListener<TrainScheduleViewModel> {
        RequestParams getRequestParam();

        void showEmptyResult();

        void clearAdapterData();

        void showDataScheduleFromCache(List<TrainScheduleViewModel> trainScheduleViewModels);

        void markSortOption();

        void addPaddingSortAndFilterSearch();

        void showFilterAndSortButtonAction();

        void hideFilterAndSortButtonAction();

        void selectSchedule(TrainScheduleViewModel trainScheduleViewModel);

        int getSortOptionSelected();

        FilterParam getFilterParam();

        void resetFilterParam();

        void showLoadingPage();

        int getScheduleVariantSelected();

        void resetFilterAndSortParam();

        void stopTrace();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getTrainSchedules();

        void getFilteredAndSortedSchedules();

        void reSearch();
    }
}
