package com.tokopedia.core.network.validator;

import com.google.gson.Gson;
import com.tokopedia.cacheapi.util.CacheApiResponseValidator;
import com.tokopedia.core.network.retrofit.response.BaseResponseError;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by nathan on 1/26/18.
 */

@Deprecated
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
