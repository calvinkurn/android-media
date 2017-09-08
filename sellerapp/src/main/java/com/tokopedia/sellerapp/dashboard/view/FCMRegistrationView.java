package com.tokopedia.sellerapp.dashboard.view;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by User on 9/8/2017.
 */

public interface FCMRegistrationView extends CustomerView {

    void onSuccessFCMRegistration(String gcmId);

    void onErrorFCMRegistration(String errorMessage);
}
