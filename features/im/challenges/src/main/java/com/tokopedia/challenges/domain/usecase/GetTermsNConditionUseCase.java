package com.tokopedia.challenges.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.challenges.data.IndiAuthInterceptor;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */
public class GetTermsNConditionUseCase extends RestRequestSupportInterceptorUseCase {


    private String collectionID;

    @Inject
    public GetTermsNConditionUseCase(IndiAuthInterceptor interceptor, @ApplicationContext Context context) {
        super(interceptor, context);
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        RestRequest restRequest1 = new RestRequest.Builder(ChallengesUrl.INDI_DOMAIN + String.format(ChallengesUrl.PRIVATE.Upload.TERMS_N_CONDITIONS, collectionID), TermsNCondition.class)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }

    public void setCollectionID(String collectionID) {
        this.collectionID = collectionID;
    }
}
