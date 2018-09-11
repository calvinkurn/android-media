package com.tokopedia.mitra.homepage.contract;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.mitra.homepage.domain.model.CategoryRow;

import java.util.List;

public interface MitraHomepageContract {
    interface View extends CustomerView {

        void showLoginContainer();

        void hideLoginContainer();

        void showMessageInRedSnackBar(@StringRes int resId);

        void navigateToLoginPage();

        void renderCategories(List<CategoryRow> categoryRows);

        Activity getActivity();

        void navigateToNextPage(Intent applinkIntent);
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();

        void onLoginResultReceived();

        void onLoginBtnClicked();

        void onApplinkReceive(String applink);
    }
}
