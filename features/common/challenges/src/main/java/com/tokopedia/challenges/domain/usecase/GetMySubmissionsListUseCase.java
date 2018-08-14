package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetMySubmissionsListUseCase extends RestRequestSupportInterceptorUseCase {

    @Inject
    public GetMySubmissionsListUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        //Request 1

        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.Me.SUBMISSIONS + "&status=approved", SubmissionResponse.class)
                .build();

//        RestRequest restRequest2 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.Me.SUBMISSIONS + "&status=waiting", SubmissionResponse.class)
//                .build();
//
//
//        RestRequest restRequest3 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.Me.SUBMISSIONS + "&status=declined", SubmissionResponse.class)
//                .build();


        tempRequest.add(restRequest1);
        //tempRequest.add(restRequest2);
        //tempRequest.add(restRequest3);

        return tempRequest;
    }
}
