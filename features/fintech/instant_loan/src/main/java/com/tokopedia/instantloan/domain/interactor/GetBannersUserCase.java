package com.tokopedia.instantloan.domain.interactor;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.instantloan.data.model.response.ResponseBannerOffer;
import com.tokopedia.instantloan.network.InstantLoanUrl;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by lavekush on 21/03/18.
 */

public class GetBannersUserCase extends RestRequestSupportInterceptorUseCase {


    public GetBannersUserCase(Interceptor interceptor, Context context) {
        super(interceptor, context);
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {

        List<RestRequest> restRequestList = new ArrayList<>();

        Type typeOfT = new TypeToken<ResponseBannerOffer>() {
        }.getType();

        RestCacheStrategy restCacheStrategy = new RestCacheStrategy.Builder(CacheType.CACHE_FIRST).build();
        RestRequest restRequest = new RestRequest.Builder(InstantLoanUrl.PATH_BANNER_OFFER, typeOfT)
                .setCacheStrategy(restCacheStrategy)
                .build();

        restRequestList.add(restRequest);
        return restRequestList;

    }
}
