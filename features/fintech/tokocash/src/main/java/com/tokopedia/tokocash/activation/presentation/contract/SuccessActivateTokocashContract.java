package com.tokopedia.tokocash.activation.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nabillasabbaha on 16/08/18.
 */
public interface SuccessActivateTokocashContract {

    interface View extends CustomerView {

        void failedRefreshToken(Throwable e);

        void showUserPhoneNumber(String phoneNumber);
    }

    interface Presenter extends CustomerPresenter<View> {

        void deleteCacheBalanceTokoCash();

        void getUserPhoneNumber();

        void refreshingWalletToken();

        void onDestroyView();
    }
}
