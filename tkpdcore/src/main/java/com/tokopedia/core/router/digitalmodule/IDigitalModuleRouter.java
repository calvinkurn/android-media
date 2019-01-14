package com.tokopedia.core.router.digitalmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * @author anggaprasetiyo on 2/23/17.
 * please use DigitalRouter in digital module for the future development features
 *
 */

public interface IDigitalModuleRouter {

    boolean isSupportedDelegateDeepLink(String appLinks);

    void actionNavigateByApplinksUrl(Activity activity, String applinks, Bundle bundle);

    Intent getLoginIntent(Context context);
}
