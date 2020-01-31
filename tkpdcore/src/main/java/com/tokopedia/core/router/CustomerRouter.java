package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * @author by alvarisi on 11/24/16.
 */

public class CustomerRouter {

    public static Intent getSplashScreenIntent(Context context) {
        return RouterUtils.getRouterFromContext(context).getSplashScreenIntent(context);
    }

    public interface IrisInstallRouter {
        void sendIrisInstallEvent();
    }

}