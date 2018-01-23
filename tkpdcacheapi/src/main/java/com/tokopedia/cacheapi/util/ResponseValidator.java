package com.tokopedia.cacheapi.util;

import com.tokopedia.cacheapi.constant.CacheApiConstant;

import okhttp3.Response;

/**
 * Created by nathan on 1/23/18.
 */

public abstract class ResponseValidator {

    private Response response;

    public boolean isResponseValidToBeCached(Response response) {
        this.response = response;
        return isResponseCodeValid();
    }

    private boolean isResponseCodeValid() {
        return response.code() == CacheApiConstant.CODE_OK;
    }
}
