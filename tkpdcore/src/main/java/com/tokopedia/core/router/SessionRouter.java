package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {
    Intent getTrueCallerIntent(Context context);
}
