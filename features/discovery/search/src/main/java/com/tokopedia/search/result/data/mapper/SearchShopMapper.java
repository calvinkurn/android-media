package com.tokopedia.search.result.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.search.result.domain.model.SearchShopModel;

import retrofit2.Response;
import rx.functions.Func1;

final class SearchShopMapper implements Func1<Response<String>, SearchShopModel> {

    private Gson gson;

    private SearchShopMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public SearchShopModel call(Response<String> stringResponse) {
        return null;
    }
}
