package com.tokopedia.tokopoints.view.contract;

import android.content.Context;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CouponFilterItem;

import java.util.List;

public interface CouponActivityContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onSuccess(List<CouponFilterItem> data);

        void onError(String error);

        Context getAppContext();

        Context getActivityContext();

        String getStringRaw(@RawRes int id);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getFilter(String slug);
    }
}
