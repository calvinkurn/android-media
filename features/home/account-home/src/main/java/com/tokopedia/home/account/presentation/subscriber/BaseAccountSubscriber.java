package com.tokopedia.home.account.presentation.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;

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
        showErrorMessage(e);
        view.hideLoading();
    }

    protected void showErrorMessage(Throwable e) {
        if(e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
            view.showErroNoConnection();
        } else {
            view.showError(e);
        }
    }
}
