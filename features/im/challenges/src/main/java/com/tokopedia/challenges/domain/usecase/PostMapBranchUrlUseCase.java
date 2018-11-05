package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class PostMapBranchUrlUseCase extends RestRequestSupportInterceptorUseCase {


    private RequestParams requestParams;
    private static final String SUBMISSION_ID_KEY = "submission_id";
    private static final String SHARE_URL_KEY = "share_url";
    private String id;
    private String shareUrl;

    @Inject
    public PostMapBranchUrlUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    public PostMapBranchUrlUseCase setRequestParams(String id, String shareUrl) {
        this.id = id;
        this.shareUrl = shareUrl;
        return this;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        Map<String, String> body = new HashMap<>();

        String url = ChallengesUrl.INDI_DOMAIN + ChallengesUrl.MANAGE.SHARE_URL_MAP;

        body.put(SUBMISSION_ID_KEY, id);
        body.put(SHARE_URL_KEY, shareUrl);

        RestRequest restRequest1 = new RestRequest.Builder(url, SubmissionResponse.class)
                .setRequestType(RequestType.POST)
                .setBody(body)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
