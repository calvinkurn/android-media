package com.tokopedia.cacheapi.util;

import com.tokopedia.cacheapi.constant.CacheApiConstant;

import java.net.HttpURLConnection;

import okhttp3.Response;

/**
 * Created by nathan on 1/23/18.
 */

public class CacheApiResponseValidator {

    private Response response;

    public boolean isResponseValidToBeCached(Response response) {
        this.response = response;
        return isResponseCodeValid();
    }

    /**
     * Only accept response code 2xx
     * @return
     */
    private boolean isResponseCodeValid() {
        switch (response.code()) {
            case HttpURLConnection.HTTP_OK:
            case HttpURLConnection.HTTP_CREATED:
            case HttpURLConnection.HTTP_ACCEPTED:
            case HttpURLConnection.HTTP_NOT_AUTHORITATIVE:
            case HttpURLConnection.HTTP_NO_CONTENT:
            case HttpURLConnection.HTTP_RESET:
            case HttpURLConnection.HTTP_PARTIAL:
                return true;
            default:
                return false;
        }
    }
}
