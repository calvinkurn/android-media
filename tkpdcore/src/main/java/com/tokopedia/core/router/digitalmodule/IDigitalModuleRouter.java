package com.tokopedia.core.router.digitalmodule;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.router.digitalmodule.passdata.DigitalCategoryDetailPassData;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;

/**
 * @author anggaprasetiyo on 2/23/17.
 */

public interface IDigitalModuleRouter {

    int REQUEST_CODE_CART_DIGITAL = 216;
    int REQUEST_CODE_LOGIN = 221;
    int REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER = 217;
    int REQUEST_CODE_DIGITAL_OPERATOR_CHOOSER = 218;
    int REQUEST_CODE_DIGITAL_PRODUCT_DETAIL = 220;
    int REQUEST_CODE_CONTACT_PICKER = 219;
    int REQUEST_CODE_DIGITAL_CATEGORY_LIST = 222;
    int REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 223;

    String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    int PAYMENT_SUCCESS = 5;
    int PAYMENT_CANCELLED = 6;
    int PAYMENT_FAILED = 7;

    BroadcastReceiver getBroadcastReceiverTokocashPending();

    Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData);

    Intent instanceIntentCartDigitalProductWithBundle(Bundle bundle);

    Intent instanceIntentDigitalProduct(DigitalCategoryDetailPassData passData);

    Intent instanceIntentDigitalCategoryList();

    Intent instanceIntentDigitalWeb(String url);

    boolean isSupportedDelegateDeepLink(String appLinks);

    Intent getIntentDeepLinkHandlerActivity();

    void actionNavigateByApplinksUrl(Activity activity, String applinks, Bundle bundle);

    Intent getLoginIntent(Context context);
}
