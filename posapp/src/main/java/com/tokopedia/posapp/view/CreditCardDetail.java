package com.tokopedia.posapp.view;

import com.tokopedia.posapp.view.viewmodel.card.PaymentViewModel;

/**
 * Created by okasurya on 10/6/17.
 */

public interface CreditCardDetail {
    interface Presenter {
        void getPaymentData(String data);
    }

    interface View {
        void onGetPaymentData(PaymentViewModel paymentViewModel);

        void onError(String message);
    }
}
