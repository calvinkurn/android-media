package com.tokopedia.abstraction;

import android.app.Activity;

/**
 * Created by nathan on 10/16/17.
 */

public interface AbstractionRouter {

    void goToForceUpdate(Activity activity);

    void onForceLogout(Activity activity);
}
