package com.tokopedia.posapp.common;

import com.google.gson.Gson;
import com.tokopedia.cacheapi.util.CacheApiResponseValidator;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author okasurya on 5/24/18.
 */
public class PosCacheApiResponseValidator extends CacheApiResponseValidator {
    private static final long BYTE_COUNT = Long.MAX_VALUE;
    private Gson gson;

    public PosCacheApiResponseValidator(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean isResponseValidToBeCached(Response response) {
        if (super.isResponseValidToBeCached(response)) {
            try {
                ResponseBody responseBody = response.peekBody(BYTE_COUNT);
                PosValidResponse validResponse = gson.fromJson(responseBody.string(), PosValidResponse.class);
                return validResponse.isValid();
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}
