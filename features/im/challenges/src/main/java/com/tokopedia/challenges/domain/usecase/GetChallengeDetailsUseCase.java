package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class GetChallengeDetailsUseCase extends RestRequestSupportInterceptorUseCase {



    @Inject
    public GetChallengeDetailsUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        HashMap headers = new HashMap();
        if (ChallengesCacheHandler.CHALLENGES_DETAILS_CACHE) {
            headers.put("Cache-Control", "max-age=0");
            ChallengesCacheHandler.setChallengesDetailsCache();
        }
        HashMap<String, Object> parameters=requestParams.getParameters();
        String challengeID = (String) parameters.get(Utils.QUERY_PARAM_CHALLENGE_ID);
        parameters.remove(Utils.QUERY_PARAM_CHALLENGE_ID);
        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.CHALLENGES_DETAILS, challengeID), Result.class)
                .setQueryParams(parameters).setHeaders(headers)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}