package com.tokopedia.affiliate.feature.tracking.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public class AffContract {

    public interface View extends CustomerView {

        void handleLink(String url);

        void finishActivity();

        void handleError();
    }

    public interface Presenter extends CustomerPresenter<View> {

        void getTrackingUrl(String affName, String urlKey);

    }
}
