package com.tokopedia.applink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author ricoharisin .
 */

public interface ApplinkRouter {
    String EXTRA_APPLINK = "EXTRA_APPLINK";

    void goToApplinkActivity(Context context, String applink);

    void goToApplinkActivity(Activity activity, String applink, Bundle bundle);

    Intent getApplinkIntent(Context context, String applink);

    boolean isSupportApplink(String appLink);
}
