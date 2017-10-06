package com.tokopedia.posapp.view;

import com.tokopedia.posapp.view.viewmodel.OtpData;

/**
 * Created by okasurya on 10/5/17.
 */

public interface Otp {
    interface Presenter {
        void initializeData(String jsonData);
    }

    interface View {
        void getOTPWebview(OtpData data);

        void postOTPWebview(OtpData data);

        void onLoadDataError(Throwable e);
    }
}
