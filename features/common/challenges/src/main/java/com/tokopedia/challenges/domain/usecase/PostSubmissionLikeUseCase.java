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
import java.util.List;

import javax.inject.Inject;

public class PostSubmissionLikeUseCase extends RestRequestSupportInterceptorUseCase {


    private RequestParams requestParams;
    public static final String IS_LIKED = "IS_LIKED";

    @Inject
    public PostSubmissionLikeUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    public void setRequestParams(RequestParams requestParams) {
        this.requestParams = requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        boolean setLiked = requestParams.getBoolean(IS_LIKED, false);
        String url;

        if (setLiked)
            url = ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.SUBMISSIONS_LIKE, requestParams.getString(Utils.QUERY_PARAM_SUBMISSION_ID, ""));
        else {
            url = ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.SUBMISSIONS_UNLIKE, requestParams.getString(Utils.QUERY_PARAM_SUBMISSION_ID, ""));

        }

        RestRequest restRequest1 = new RestRequest.Builder(url, SubmissionResponse.class)
                .setRequestType(RequestType.POST)
                .setBody("")
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
