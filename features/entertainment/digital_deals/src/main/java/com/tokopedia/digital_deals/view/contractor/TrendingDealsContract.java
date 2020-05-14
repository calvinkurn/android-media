package com.tokopedia.digital_deals.view.contractor;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.usecase.RequestParams;

public class TrendingDealsContract {

    public interface View extends CustomerView {

        RequestParams getParams();
    }

    public interface Presenter extends CustomerPresenter<TrendingDealsContract.View> {

        void onDestroy();

    }
}
