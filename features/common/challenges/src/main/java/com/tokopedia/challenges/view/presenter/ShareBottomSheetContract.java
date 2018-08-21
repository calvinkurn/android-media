package com.tokopedia.challenges.view.presenter;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public class ShareBottomSheetContract {
    public interface View extends CustomerView {
        Activity getActivity();

        void setNewUrl(String shareUri);

        void showProgress(String message);

        void hideProgress();
    }

    public interface Presenter extends CustomerPresenter<View> {
    }
}
