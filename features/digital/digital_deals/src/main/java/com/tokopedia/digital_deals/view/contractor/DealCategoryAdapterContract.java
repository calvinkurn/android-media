package com.tokopedia.digital_deals.view.contractor;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.usecase.RequestParams;

public class DealCategoryAdapterContract {

    public interface View extends CustomerView {

        Activity getActivity();

        RequestParams getParams();

        void notifyDataSetChanged(int position);

        void showLoginSnackbar(String message);

    }

    public interface Presenter extends CustomerPresenter<DealCategoryAdapterContract.View> {

        void onDestroy();

    }
}
