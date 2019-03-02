package com.tokopedia.affiliate.common.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.data.pojo.AffiliateCheckData;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by milhamj on 10/17/18.
 */
public class CheckAffiliateUseCase extends UseCase<Boolean> {

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public CheckAffiliateUseCase(@ApplicationContext Context context,
                                 GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_affiliate_check),
                AffiliateCheckData.class
        );
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(mapIsAffiliate());
    }

    private Func1<GraphqlResponse, Boolean> mapIsAffiliate() {
        return graphqlResponse -> {
            AffiliateCheckData data = graphqlResponse.getData(AffiliateCheckData.class);
            if (data == null || data.getAffiliateCheck() == null) {
                throw new RuntimeException();
            }

            return data.getAffiliateCheck().isIsAffiliate();
        };
    }
}
