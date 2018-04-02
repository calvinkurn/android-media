package com.tokopedia.gamification;

import okhttp3.Interceptor;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public interface GamificationRouter {

    Interceptor getChuckInterceptor();
}
