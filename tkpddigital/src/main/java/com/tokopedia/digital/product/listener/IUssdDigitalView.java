package com.tokopedia.digital.product.listener;

import android.app.Activity;
import android.app.Application;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.digital.cart.listener.IBaseView;

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
