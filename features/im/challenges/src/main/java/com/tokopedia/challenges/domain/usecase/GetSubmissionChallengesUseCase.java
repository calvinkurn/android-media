package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.Utils;
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

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class GetSubmissionChallengesUseCase extends RestRequestSupportInterceptorUseCase {

    @Inject
    public GetSubmissionChallengesUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    @Override
    public Observable<Map<Type, RestResponse>> createObservable(RequestParams requestParams) {
        return super.createObservable(requestParams).map(typeRestResponseMap -> {

            if (ChallengesCacheHandler.MANIPULATED_ELEMENTS_MAP.size() > 0) {
                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse mainDataObject = res1.getData();
                List<SubmissionResult> submissionResultList = mainDataObject.getSubmissionResults();
                if (submissionResultList != null && submissionResultList.size() > 0) {

                    for (int i = 0; i < submissionResultList.size(); i++) {
                        SubmissionResult submissionResult = submissionResultList.get(i);
                        int value = ChallengesCacheHandler.getManipulatedValue(submissionResult.getId());
                        if (value == ChallengesCacheHandler.Manupulated.NOTFOUND.ordinal()) {
                            continue;
                        } else if (value == ChallengesCacheHandler.Manupulated.DELETE.ordinal()) {
                            submissionResultList.remove(submissionResult);

                        } else if (value == ChallengesCacheHandler.Manupulated.LIKE.ordinal()) {
                            submissionResult.getMe().setLiked(true);

                        } else {
                            submissionResult.getMe().setLiked(false);

                        }
                    }


                }
            }

            return typeRestResponseMap;
        });
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        HashMap headers = new HashMap();
        if (ChallengesCacheHandler.CHALLENGES_SUBMISSTIONS_LIST_CACHE) {
            headers.put("Cache-Control", "max-age=0");
            ChallengesCacheHandler.setChallengeSubmissionssListCache();
        }
        //Request 1

        HashMap<String, Object> parameters = requestParams.getParameters();
        String challengeID = (String) parameters.get(Utils.QUERY_PARAM_CHALLENGE_ID);
        parameters.remove(Utils.QUERY_PARAM_CHALLENGE_ID);
        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.CHALLENGES_SUBMISSIONS, challengeID), SubmissionResponse.class)
                .setQueryParams(parameters)
                .setHeaders(headers).build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}