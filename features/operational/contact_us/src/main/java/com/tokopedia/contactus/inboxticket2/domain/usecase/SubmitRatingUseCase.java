package com.tokopedia.contactus.inboxticket2.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class SubmitRatingUseCase  extends UseCase<ChipGetInboxDetail> {
    Context context;
    GraphqlUseCase graphqlUseCase;

    @Inject
    public SubmitRatingUseCase(Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void createRequestParams(String commentID, String rating, String reason) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("comment_id",commentID);
        requestParams.putString("rating",rating);
        requestParams.putString("reason",reason);
    }

    @Override
    public Observable<ChipGetInboxDetail> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.submit_rating), ChipGetInboxDetail.class, requestParams.getParameters());
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(requestParams).map(new Func1<GraphqlResponse, ChipGetInboxDetail>() {
            @Override
            public ChipGetInboxDetail call(GraphqlResponse graphqlResponse) {
                return graphqlResponse.getData(ChipGetInboxDetail.class);
            }
        });
    }
}