package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */
public class PostDeleteSubmissionUseCase extends RestRequestSupportInterceptorUseCase {


    private String postId;

    @Inject
    public PostDeleteSubmissionUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    public void setRequestParams(String postId) {
        this.postId = postId;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();


        String url = ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.Upload.DELETE_POST, postId);
        RestRequest restRequest1 = new RestRequest.Builder(url, Object.class)
                .setRequestType(RequestType.POST)
                .setBody("")
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
