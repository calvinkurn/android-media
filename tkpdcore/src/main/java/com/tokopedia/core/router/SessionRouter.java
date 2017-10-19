package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {
    Intent getTrueCallerIntent(Context context);
}
