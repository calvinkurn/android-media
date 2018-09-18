package com.tokopedia.browse.common;

import android.app.Activity;
import android.content.Intent;

/**
 * @author by furqan on 30/08/18.
 */

public interface DigitalBrowseRouter {

    Intent getWebviewActivity(Activity activity, String url);

}
