package com.tokopedia.events;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.util.SessionHandler;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public interface EventModuleRouter {

    public Interceptor getChuckInterceptor();

    public OkHttpClient getOkHttpClient();

    public UserSession getSession();

    public SessionHandler getSessionHandler();
}
