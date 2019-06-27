package com.tokopedia.flight.orderlist.view.contract;

import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * @author by furqan on 08/02/18.
 */

public interface FlightResendETicketContract {
    interface View extends CustomerView {
        String getInvoiceId();

        String getUserId();

        String getEmail();

        void showEmailEmptyError(@StringRes int resId);

        void showEmailInvalidError(@StringRes int resId);

        void showEmailInvalidSymbolError(@StringRes int resId);

        void onResendETicketSuccess();

        void onResendETicketError(String errorMsg);

        Context getActivity();
    }

    interface Presenter {
        void onSendButtonClicked();
    }
}
