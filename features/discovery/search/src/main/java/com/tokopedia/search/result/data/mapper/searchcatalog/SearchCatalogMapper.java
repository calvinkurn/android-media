package com.tokopedia.search.result.data.mapper.searchcatalog;

import com.google.gson.Gson;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.network.constant.ResponseStatus;
import com.tokopedia.search.result.data.response.SearchCatalogResponse;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;

import retrofit2.Response;
import rx.functions.Func1;

final class SearchCatalogMapper implements Func1<Response<String>, SearchCatalogModel> {

    private final Gson gson;

    SearchCatalogMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public SearchCatalogModel call(Response<String> response) {
        if (response.isSuccessful()) {
            SearchCatalogResponse searchCatalogResponse = gson.fromJson(response.body(), SearchCatalogResponse.class);

            if (searchCatalogResponse != null) {
                return searchCatalogResponse.result;
            } else {
                throw new RuntimeException("searchCatalogResponse is null");
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
