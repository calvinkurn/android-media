package com.tokopedia.posapp.payment.otp;

import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.posapp.payment.otp.view.viewmodel.OTPData;

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
