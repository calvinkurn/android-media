package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

public class GetActiveChallengesUseCase extends RestRequestSupportInterceptorUseCase {

    @Inject
    public GetActiveChallengesUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();
        HashMap headers = new HashMap();
        if (ChallengesCacheHandler.OPEN_CHALLENGES_LIST_CACHE) {
            headers.put("Cache-Control", "max-age=0");
            ChallengesCacheHandler.setOpenChallengesListCache();
        }

        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + ChallengesUrl.PRIVATE.OPEN_CHALLENGES, Challenge.class)
                .setHeaders(headers).build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
