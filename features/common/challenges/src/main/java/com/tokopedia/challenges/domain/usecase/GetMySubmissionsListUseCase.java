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

    private int start=0;
    private int size=10;

    @Inject
    public GetMySubmissionsListUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    public GetMySubmissionsListUseCase setRequestParams(int start, int size) {
        this.start = start;
        this.size = size;
        return this;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.Me.SUBMISSIONS + "&status=all&start="+start+"&size="+size, SubmissionResponse.class)
                .build();

        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
