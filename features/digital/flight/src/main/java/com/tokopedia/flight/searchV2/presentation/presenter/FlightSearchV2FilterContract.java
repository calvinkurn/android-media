package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel;

/**
 * Created by Rizky on 17/10/18.
 */
public class FlightSearchV2FilterContract {

    public interface View extends CustomerView {

        void onErrorGetCount(Throwable throwable);

        void onSuccessGetCount(int count);

        boolean isReturning();

        void showErrorGetFilterStatistic(Throwable e);

        void onSuccessGetStatistic(FlightSearchStatisticModel statisticModel);

        void showGetFilterStatisticLoading();

        void hideGetFilterStatisticLoading();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getFilterStatisticData();

        void getFlightCount(boolean isReturning, boolean b, FlightFilterModel flightFilterModel);

    }


}
