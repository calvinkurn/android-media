package com.tokopedia.digital.common.router;

import android.app.Activity;
import android.content.Intent;

/**
 * @author by alvarisi on 2/20/18.
 */

public interface DigitalModuleRouter {

    Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle);
}
