package com.tokopedia.home_account.account_settings.presentation.subscriber;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.home_account.account_settings.presentation.listener.BaseAccountView;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author okasurya on 8/23/18.
 */
public abstract class BaseAccountSubscriber<T> extends Subscriber<T> {
    private BaseAccountView view;

    protected BaseAccountSubscriber(BaseAccountView view) {
        this.view = view;
    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        showErrorMessage(e, getErrorCode());
        view.hideLoading();
    }

    protected void showErrorMessage(Throwable e, String errorCode) {
        if(e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
            view.showErrorNoConnection();
        } else {
            view.showError(e, errorCode);
        }
    }

    abstract String getErrorCode();
}
