package com.tokopedia.flight.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel;

import java.util.List;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassesContract {
    public interface View extends CustomerView {

        void showFetchClassesLoading();

        void hideFetchClassesLoading();

        void renderFlightClasses(List<FlightClassModel> transform);
    }

    interface Presenter extends CustomerPresenter<View> {

        void actionFetchClasses();
    }
}
