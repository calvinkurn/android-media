package com.tokopedia.posapp.view;

import com.tokopedia.posapp.domain.model.payment.PaymentStatusDomain;
import com.tokopedia.posapp.view.viewmodel.otp.OTPData;

import java.util.List;

/**
 * Created by okasurya on 10/5/17.
 */

public interface OTP {
    interface Presenter {
        void initializeData(String jsonData);

        void processPayment();

        void setTransactionId(String transactionId);
    }

    interface View {
        void getOTPWebview(OTPData data);

        void postOTPWebview(OTPData data);

        void onLoadDataError(String errorMessage);

        void onPaymentError(Throwable e);

        void onPaymentCompleted(PaymentStatusDomain paymentStatusDomain);
    }
}
