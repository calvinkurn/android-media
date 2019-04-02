package com.tokopedia.flight.search.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightFilterCountView extends CustomerView {
    void onErrorGetCount(Throwable throwable);

    void onSuccessGetCount(int count);

    boolean isReturning();

    void showErrorGetFilterStatistic(Throwable e);

    void onSuccessGetStatistic(FlightSearchStatisticModel statisticModel);

    void showGetFilterStatisticLoading();

    void hideGetFilterStatisticLoading();
}
