package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class GetSubmissionChallengesUseCase extends RestRequestSupportInterceptorUseCase {


    private RequestParams requestParams;

    @Inject
    public GetSubmissionChallengesUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    public void setRequestParams(RequestParams requestParams){
        this.requestParams=requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        //Request 1

        HashMap<String, Object> parameters=requestParams.getParameters();
        ChallengesUrl.CHALLENGE_ID = String.valueOf(parameters.get(Utils.QUERY_PARAM_CHALLENGE_ID));
        parameters.remove(Utils.QUERY_PARAM_CHALLENGE_ID);
        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.PRIVATE.CHALLENGES_SUBMISSIONS, SubmissionResponse.class)
                .setQueryParams(parameters)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
