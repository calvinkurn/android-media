package com.tokopedia.applink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @author ricoharisin .
 */

public interface ApplinkRouter {
    String EXTRA_APPLINK_UNSUPPORTED = "EXTRA_APPLINK_UNSUPPORTED";
    String EXTRA_APPLINK = "EXTRA_APPLINK";

    void goToApplinkActivity(Context context, String applink);

    Intent getApplinkIntent(Context context, String applink);

    boolean isSupportApplink(String appLink);

    ApplinkUnsupported getApplinkUnsupported(Activity activity);

    void dispatchFrom(Activity activity, Intent intent);
}
