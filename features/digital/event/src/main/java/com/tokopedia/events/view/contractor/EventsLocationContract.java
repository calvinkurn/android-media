package com.tokopedia.events.view.contractor;

import android.app.Activity;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.viewmodel.EventLocationViewModel;

import java.util.List;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class EventsLocationContract {
    public interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void renderLocationList(List<EventLocationViewModel> eventLocationViewModels);

        RequestParams getParams();

    }

    public interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void onDestroy();
    }
}
