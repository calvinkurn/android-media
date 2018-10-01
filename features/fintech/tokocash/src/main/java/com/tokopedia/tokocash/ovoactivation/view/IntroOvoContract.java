package com.tokopedia.tokocash.ovoactivation.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public interface IntroOvoContract {

    interface View extends CustomerView {

        void setApplinkButton(String helpAllink, String tncApplink);

        void directPageWithApplink(String registeredApplink);

        void directPageWithExtraApplink(String unRegisteredApplink, String registeredApplink, String phoneNumber,
                                        String changeMsisdnApplink);

        void showSnackbarErrorMessage(String message);

        String getErrorMessage(Throwable e);

        void showProgressBar();

        void hideProgressBar();

        void showDialogErrorPhoneNumber(PhoneActionModel phoneActionModel);
    }

    interface Presenter extends CustomerPresenter<View> {

        void checkPhoneNumber();

        void getBalanceWallet();

        void onDestroyView();
    }
}
