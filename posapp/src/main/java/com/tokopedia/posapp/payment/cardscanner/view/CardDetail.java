package com.tokopedia.posapp.payment.cardscanner.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.posapp.payment.cardscanner.view.viewmodel.PaymentViewModel;

/**
 * Created by okasurya on 8/18/17.
 */

public interface CardDetail {
    int DATA_VALID = 0;
    int ERROR = 1;
    int ERROR_CARD_NOT_VALID = 2;
    int ERROR_CARD_NOT_MATCH_WITH_BANK = 3;
    int ERROR_CARD_NOT_VALID_FOR_INSTALLMENT = 4;
    int ERROR_DATE_NOT_VALID = 5;
    int ERROR_EMPTY_DATE = 6;
    int ERROR_INVALID_CVV = 7;

    interface Presenter extends CustomerPresenter<View> {
        void pay(PaymentViewModel creditCardViewModel);
    }

    interface View extends CustomerView {
        void onGetPayData(String paymentData);

        void onError(Throwable e);

        void onValidationError(int errorCode);
    }
}
