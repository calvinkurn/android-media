package com.tokopedia.logisticuploadawb;

import java.util.Objects;

import sun.tools.java.Type;

public interface ILogisticUploadAwbRouter<T> {

    String logisticUploadRouterGetApplicationBuildFlavor();

    boolean logisticUploadRouterIsSupportedDelegateDeepLink(String url);

    void logisticUploadRouterActionNavigateByApplinksUrl(Object activity, Object applinks, Object bundle);

}
