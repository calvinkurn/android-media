package com.tokopedia.core.router.digitalmodule;

import android.content.Intent;

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

    String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData);

    Intent instanceIntentDigitalProduct(DigitalCategoryDetailPassData passData);

    Intent instanceIntentDigitalCategoryList();

    boolean isSupportedDelegateDeepLink(String appLinks);

    Intent getIntentDeepLinkHandlerActivity();

}
