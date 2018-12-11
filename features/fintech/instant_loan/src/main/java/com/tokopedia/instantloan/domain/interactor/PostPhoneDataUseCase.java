package com.tokopedia.instantloan.domain.interactor;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.instantloan.data.model.response.PhoneDataEntity;
import com.tokopedia.instantloan.data.model.response.ResponsePhoneData;
import com.tokopedia.instantloan.network.InstantLoanUrl;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

public class PostPhoneDataUseCase extends RestRequestSupportInterceptorUseCase{
    private JsonObject body;

    public PostPhoneDataUseCase(Interceptor interceptor, Context context) {
        super(interceptor, context);
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {

        List<RestRequest> restRequestList = new ArrayList<>();
        Type typeOfT = new TypeToken<ResponsePhoneData>() {
        }.getType();

        RestCacheStrategy restCacheStrategy = new RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build();
        RestRequest restRequest = new RestRequest.Builder(InstantLoanUrl.PATH_POST_PHONEDATA, typeOfT)
                .setRequestType(RequestType.POST)
                .setCacheStrategy(restCacheStrategy)
                .setBody(this.body)
                .build();

        restRequestList.add(restRequest);
        return restRequestList;

    }

    public void setBody(JsonObject body) {
        this.body = body;
    }
}
