package com.tokopedia.instantloan.domain.interactor;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.instantloan.data.model.response.ResponseUserProfileStatus;
import com.tokopedia.instantloan.data.model.response.UserProfileLoanEntity;
import com.tokopedia.instantloan.network.InstantLoanAuthInterceptor;
import com.tokopedia.instantloan.network.InstantLoanUrl;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lavekush on 21/03/18.
 */

public class GetLoanProfileStatusUseCase extends RestRequestSupportInterceptorUseCase {

    public GetLoanProfileStatusUseCase(InstantLoanAuthInterceptor interceptor, Context context) {
        super(interceptor, context);
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {

        List<RestRequest> restRequestList = new ArrayList<>();

        Type typeOfT = new TypeToken<ResponseUserProfileStatus>(){}.getType();

        RestCacheStrategy restCacheStrategy = new RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build();
        RestRequest restRequest = new RestRequest.Builder(InstantLoanUrl.PATH_USER_PROFILE_STATUS, typeOfT)
                .setRequestType(RequestType.GET)
                .setCacheStrategy(restCacheStrategy)
                .build();

        restRequestList.add(restRequest);
        return restRequestList;
    }

}
