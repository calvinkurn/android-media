package com.tokopedia.common_digital;

import okhttp3.Interceptor;

/**
 * Created by Rizky on 13/08/18.
 */
public interface DigitalRouter {

    Interceptor getChuckInterceptor();

}
