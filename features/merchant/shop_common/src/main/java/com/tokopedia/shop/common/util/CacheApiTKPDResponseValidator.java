package com.tokopedia.shop.common.util;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.cacheapi.util.CacheApiResponseValidator;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by nathan on 1/26/18.
 */

public class CacheApiTKPDResponseValidator<T extends BaseResponseError> extends CacheApiResponseValidator {

    private static final long BYTE_COUNT = Long.MAX_VALUE;

    private Class<T> typeParameterClass;

    public CacheApiTKPDResponseValidator(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public boolean isResponseValidToBeCached(Response response) {
        if (!super.isResponseValidToBeCached(response)) {
            return false;
        }
        return !isStatusOkAndResponseError(response);
    }

    private boolean isStatusOkAndResponseError(Response response) {
        try {
            Gson gson = new Gson();
            ResponseBody responseBody = response.peekBody(BYTE_COUNT);
            BaseResponseError responseError = gson.fromJson(responseBody.string(), typeParameterClass);
            return responseError.isResponseErrorValid();
        } catch (Exception e) {
            return false;
        }
    }
}
