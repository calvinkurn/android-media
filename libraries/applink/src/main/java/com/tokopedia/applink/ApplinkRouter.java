package com.tokopedia.applink;

import android.content.Context;
import android.content.Intent;

/**
 * @author ricoharisin .
 */

public interface ApplinkRouter {

    void goToApplinkActivity(Context context, String applink);

    Intent getApplinkIntent(Context context, String applink);
}
