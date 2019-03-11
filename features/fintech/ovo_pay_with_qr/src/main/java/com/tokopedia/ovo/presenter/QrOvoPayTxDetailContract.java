package com.tokopedia.ovo.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.ovo.model.GoalQRThanks;

public interface QrOvoPayTxDetailContract {
    public interface View extends CustomerView {
        void setSuccessThankYouData(GoalQRThanks data);
        void setFailThankYouData(GoalQRThanks data);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void requestForThankYouPage(Context context, int transferId);
    }
}
