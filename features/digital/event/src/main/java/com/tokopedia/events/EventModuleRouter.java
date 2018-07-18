package com.tokopedia.events;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.network.retrofit.interceptors.EventInerceptors;
import com.tokopedia.core.util.SessionHandler;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public interface EventModuleRouter {

    public Interceptor getChuckInterceptor();

    public OkHttpClient getOkHttpClient(EventInerceptors eventInerceptors, HttpLoggingInterceptor loggingInterceptor);

    public UserSession getSession();

    public SessionHandler getSessionHandler();

}
