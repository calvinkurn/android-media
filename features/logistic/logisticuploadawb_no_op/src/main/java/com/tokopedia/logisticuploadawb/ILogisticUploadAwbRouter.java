package com.tokopedia.logisticuploadawb;

public interface ILogisticUploadAwbRouter<T> {

    String logisticUploadRouterGetApplicationBuildFlavor();

    boolean logisticUploadRouterIsSupportedDelegateDeepLink(String url);

    void logisticUploadRouterActionNavigateByApplinksUrl(Object activity, Object applinks, Object bundle);

}
