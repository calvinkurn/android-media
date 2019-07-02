package com.tokopedia.tokopoints.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity;

public interface PointHistoryContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onSuccess(TokoPointStatusPointsEntity data);

        void onError(String error);

        Context getAppContext();

        Context getActivityContext();
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getPointsDetail();

    }
}
