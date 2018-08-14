package com.tokopedia.common_digital;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

/**
 * Created by Rizky on 13/08/18.
 */
public class DigitalInterceptor extends TkpdAuthInterceptor {

    public DigitalInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession) {
        super(context, networkRouter, userSession);
    }

    public DigitalInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession, String authKey) {
        super(context, networkRouter, userSession, authKey);
    }

}