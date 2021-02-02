package com.tokopedia.linker.interfaces;

import android.content.Context;

import com.tokopedia.linker.requests.LinkerDeeplinkRequest;
import com.tokopedia.linker.requests.LinkerGenericRequest;
import com.tokopedia.linker.requests.LinkerShareRequest;

public interface WrapperInterface {
    void init(Context context);
    String getDefferedDeeplinkForSession();
    void createShareUrl(LinkerShareRequest linkerShareRequest, Context context);
    void handleDefferedDeeplink(LinkerDeeplinkRequest linkerDeeplinkRequest, Context context, boolean isReInit);
    void sendEvent(LinkerGenericRequest linkerGenericRequest, Context context);
    void setGaClientId(String gaClientId);
    void initSession();
}
