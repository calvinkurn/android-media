package com.tokopedia.cacheapi.util;

import okhttp3.Response;

/**
 * Created by nathan on 1/23/18.
 */

public interface ResponseValidation {

    boolean isResponseValidToBeCached(Response response);
}
