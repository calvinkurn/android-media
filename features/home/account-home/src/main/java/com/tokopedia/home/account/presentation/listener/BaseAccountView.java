package com.tokopedia.home.account.presentation.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by meta on 08/08/18.
 */
public interface BaseAccountView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showError(String message);

    void showError(Throwable e);

    void showErroNoConnection();

}
