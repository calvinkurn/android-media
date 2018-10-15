package com.tokopedia.digital.common.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author by alvarisi on 2/20/18.
 */

public interface DigitalModuleRouter {
    int REQUEST_CODE_DIGITAL_CATEGORY_LIST = 222;
    int REQUEST_CODE_DIGITAL_PRODUCT_DETAIL = 220;
    String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle);

    Intent getLoginIntent(Context activity);

    Intent instanceIntentDigitalCategoryList();
}
