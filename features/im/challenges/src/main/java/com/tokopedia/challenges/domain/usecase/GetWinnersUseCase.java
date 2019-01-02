package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
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

public class GetWinnersUseCase extends RestRequestSupportInterceptorUseCase {



    @Inject
    public GetWinnersUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }


    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        HashMap<String, Object> parameters = requestParams.getParameters();
        String challengeID = (String) parameters.get(Utils.QUERY_PARAM_CHALLENGE_ID);
        parameters.remove(Utils.QUERY_PARAM_CHALLENGE_ID);
        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.CHALLENGE_WINNERS, challengeID), SubmissionResponse.class)
                .setQueryParams(parameters)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}