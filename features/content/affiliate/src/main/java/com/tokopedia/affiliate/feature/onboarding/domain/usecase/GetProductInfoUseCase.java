package com.tokopedia.affiliate.feature.onboarding.domain.usecase;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by milhamj on 10/4/18.
 */
public class GetProductInfoUseCase extends GraphqlUseCase {
    @Override
    public Observable<GraphqlResponse> createObservable(RequestParams params) {
        this.clearRequest();
        this.addRequest(null);
        return super.createObservable(params);
    }
}
