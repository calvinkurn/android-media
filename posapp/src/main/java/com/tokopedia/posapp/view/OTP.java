package com.tokopedia.posapp.view;

import com.tokopedia.posapp.view.viewmodel.otp.OTPData;

import java.util.List;

/**
 * Created by okasurya on 10/5/17.
 */

public interface OTP {
    interface Presenter {
        void initializeData(String jsonData);

        void checkPaymentState(String url);
    }

    interface View {
        void getOTPWebview(OTPData data);

        void postOTPWebview(OTPData data);

        void onLoadDataError(Throwable e);

        void onLoadDataError(List<String> errorList);
    }
}
