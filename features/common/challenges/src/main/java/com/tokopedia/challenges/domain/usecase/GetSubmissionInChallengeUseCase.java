package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GetSubmissionInChallengeUseCase extends RestRequestSupportInterceptorUseCase {

  private String challengeId;

    @Inject
    public GetSubmissionInChallengeUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    public GetSubmissionInChallengeUseCase setRequestParams(String challengeId) {
        this.challengeId = challengeId;
        return this;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();
        String url = ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.Me.SUBMISSIONS_IN_CHALLENGE, challengeId) + "&status=all&start=0&size=1";
        RestRequest restRequest1 = new RestRequest.Builder(url, SubmissionResponse.class)
                .build();

        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
