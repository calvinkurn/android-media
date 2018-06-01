package com.tokopedia.instantloan.di.module;

import okhttp3.Interceptor;

/**
 * Created by sachinbansal on 6/1/18.
 */

public interface InstantLoanRouter {
    Interceptor getChuckInterceptor();
}
