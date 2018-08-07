package com.tokopedia.challenges.data;

import android.content.Context;

import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by Vishal Gupta on 8/7/18.
 */
public class IndiTestUseCase extends RestRequestSupportInterceptorUseCase {

    public IndiTestUseCase(Interceptor interceptor, Context context) {
        super(interceptor, context);
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        //Request 1

        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.PRIVATE.ME, IndiUserModel.class)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}