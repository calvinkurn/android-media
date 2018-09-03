package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class GetDetailsSubmissionsUseCase extends RestRequestSupportInterceptorUseCase {

    private String postId;

    @Inject
    public GetDetailsSubmissionsUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    public GetDetailsSubmissionsUseCase setRequestParams(String postId) {
        this.postId = postId;
        return this;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();
        HashMap headers = new HashMap();
        if (ChallengesCacheHandler.SUBMISSTION_DETAILS_CACHE) {
            headers.put("Cache-Control", "max-age=0");
            ChallengesCacheHandler.resetSubmissionsDetailsCache();
        }

        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.PRIVATE.SUBMISSIONS_DETAILS + postId, SubmissionResult.class)
                .setHeaders(headers).build();

        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
