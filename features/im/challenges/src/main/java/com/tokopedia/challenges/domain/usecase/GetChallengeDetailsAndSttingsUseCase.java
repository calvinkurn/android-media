package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class GetChallengeDetailsAndSttingsUseCase extends RestRequestSupportInterceptorUseCase {


    private String challengeId;

    @Inject
    public GetChallengeDetailsAndSttingsUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    public void setRequestParams(String challengeId){
        this.challengeId=challengeId;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();


        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.CHALLENGES_DETAILS, challengeId), Result.class)
                .build();
        tempRequest.add(restRequest1);

        RestRequest restRequest2 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.Upload.CHALLENGE_SETTING,challengeId), ChallengeSettings.class)
                .build();
        tempRequest.add(restRequest2);

        return tempRequest;
    }
}