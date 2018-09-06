package com.tokopedia.reksadana.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public class DashBoardContract {
    public interface View extends CustomerView {
        Context getAppContext();
    }

    public interface Presenter extends CustomerPresenter<View> {
        void getData();
    }
}
