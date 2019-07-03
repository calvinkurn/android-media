package com.tokopedia.search.result.data.mapper.searchshop;

import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.constant.ResponseStatus;
import com.tokopedia.search.result.data.response.SearchShopResponse;
import com.tokopedia.search.result.domain.model.SearchShopModel;

import retrofit2.Response;
import rx.functions.Func1;

final class SearchShopMapper implements Func1<Response<SearchShopResponse>, SearchShopModel> {

    SearchShopMapper() { }

    @Override
    public SearchShopModel call(Response<SearchShopResponse> response) {
        if (response.isSuccessful()) {
            SearchShopModel searchShopModel = response.body().searchShopModel;

            if (searchShopModel != null) {
                return searchShopModel;
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
