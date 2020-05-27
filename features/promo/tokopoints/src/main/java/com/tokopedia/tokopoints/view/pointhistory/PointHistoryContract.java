package com.tokopedia.tokopoints.view.pointhistory;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity;

public interface PointHistoryContract {
    interface View extends CustomerView {
        void showLoading();

        void onSuccess(TokoPointStatusPointsEntity data);

        void onError(String error);

        Context getAppContext();

        Context getActivityContext();
    }
}
