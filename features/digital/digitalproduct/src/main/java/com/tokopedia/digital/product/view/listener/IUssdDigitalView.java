package com.tokopedia.digital.product.view.listener;

import android.app.Activity;
import android.app.Application;

import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.cart.presentation.listener.IBaseView;

/**
 * Created by ashwanityagi on 20/07/17.
 */

public interface IUssdDigitalView extends IBaseView {

    boolean isUserLoggedIn();

    Application getMainApplication();

    String getUserLoginId();

    String getVersionInfoApplication();

    void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData);

    Activity getActivity();

}
