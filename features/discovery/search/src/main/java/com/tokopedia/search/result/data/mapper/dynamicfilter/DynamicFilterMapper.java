package com.tokopedia.search.result.data.mapper.dynamicfilter;

import com.google.gson.Gson;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.constant.ResponseStatus;

import retrofit2.Response;
import rx.functions.Func1;

final class DynamicFilterMapper implements Func1<Response<String>, DynamicFilterModel> {

    private final Gson gson;

    DynamicFilterMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public DynamicFilterModel call(Response<String> response) {
        if (response.isSuccessful()) {
            DynamicFilterModel pojo = gson.fromJson(response.body(), DynamicFilterModel.class);
            if (pojo != null) {
                return pojo;
            } else {
                throw new RuntimeException(response.errorBody().toString());
            }
        } else {
            throw new RuntimeException(getRuntimeErrorExceptionMessage(response.code()));
        }
    }

    private String getRuntimeErrorExceptionMessage(int errorCode) {
        String messageError;

        switch (errorCode) {
            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                messageError = ErrorNetMessage.MESSAGE_ERROR_SERVER;
                break;
            case ResponseStatus.SC_FORBIDDEN:
                messageError = ErrorNetMessage.MESSAGE_ERROR_FORBIDDEN;
                break;
            case ResponseStatus.SC_REQUEST_TIMEOUT:
            case ResponseStatus.SC_GATEWAY_TIMEOUT:
                messageError = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                break;
            default:
                messageError = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                break;
        }

        return messageError;
    }
}
