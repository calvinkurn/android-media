package com.tokopedia.logisticuploadawb;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author anggaprasetiyo on 22/05/18.
 */
public interface ILogisticUploadAwbRouter {

    String logisticUploadRouterGetApplicationBuildFlavor();

    boolean logisticUploadRouterIsSupportedDelegateDeepLink(String url);

    void logisticUploadRouterActionNavigateByApplinksUrl(Activity activity, String applinks, Bundle bundle);
}
