package com.tokopedia.flight.searchV2.presentation.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightSearchTestingContract {

    public interface View extends CustomerView {

    }

    public interface Presenter extends CustomerPresenter<View> {

    }

}
