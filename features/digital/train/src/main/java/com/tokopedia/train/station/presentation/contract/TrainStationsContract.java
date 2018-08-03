package com.tokopedia.train.station.presentation.contract;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.List;

/**
 * Created by alvarisi on 3/5/18.
 */

public interface TrainStationsContract {
    interface View extends CustomerView {

        void renderStationList(Visitable visitable);

        void renderStationList(List<Visitable> visitables);

        void clearStationList();

        void showLoading();

        void hideLoading();

        void renderSearchHint();

        void hideSearchView();

        void showSearchView();
    }

    interface Presenter extends CustomerPresenter<View> {

        void actionOnInitialLoad();

        void onKeywordChange(String keyword);

        void onViewCreated();
    }
}
