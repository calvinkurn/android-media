package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
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

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class GetMySubmissionsListUseCase extends RestRequestSupportInterceptorUseCase {

    private int start = 0;
    private int size = 10;

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
    public Observable<Map<Type, RestResponse>> createObservable(RequestParams requestParams) {
        return super.createObservable(requestParams).map(typeRestResponseMap -> {

            if (ChallengesCacheHandler.MANIPULATED_ELEMENTS_MAP.size() > 0) {
                RestResponse res1 = typeRestResponseMap.get(SubmissionResponse.class);
                SubmissionResponse mainDataObject = res1.getData();
                List<SubmissionResult> submissionResultList = mainDataObject.getSubmissionResults();
                if (submissionResultList != null && submissionResultList.size() > 0) {
                    List<SubmissionResult> submissionResultListTemp=new ArrayList<>();
                    for (SubmissionResult submissionResult:submissionResultList) {
                        int value = ChallengesCacheHandler.getManipulatedValue(submissionResult.getId());
                        if (value == ChallengesCacheHandler.Manupulated.DELETE.ordinal()) {
                           continue;
                        } else if (value == ChallengesCacheHandler.Manupulated.LIKE.ordinal()) {
                            submissionResult.getMe().setLiked(true);
                        } else {
                            submissionResult.getMe().setLiked(false);
                        }
                        submissionResultListTemp.add(submissionResult);
                    }
                    mainDataObject.setSubmissionResults(submissionResultListTemp);
                }
            }

            return typeRestResponseMap;
        });
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        HashMap headers = new HashMap();
        String url = ChallengesUrl.INDI_DOMAIN + ChallengesUrl.Me.SUBMISSIONS + "&status=all&start=" + start + "&size=" + size;
        if (ChallengesCacheHandler.MY_SUBMISSTIONS_LIST_CACHE) {
            headers.put("Cache-Control", "max-age=0");
            ChallengesCacheHandler.setMySubmissionsListCache();
        }

        RestRequest restRequest1 = new RestRequest.Builder(url, SubmissionResponse.class).setHeaders(headers)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
