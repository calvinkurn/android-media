package com.tokopedia.contactus.inboxticket2.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail;
import com.tokopedia.contactus.inboxticket2.data.model.ChipInboxDetails;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class InboxOptionUseCase extends UseCase<ChipGetInboxDetail> {
    Context context;
    GraphqlUseCase graphqlUseCase;
    RequestParams requestParams;

    @Inject
    public InboxOptionUseCase(Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void createRequestParams(String id) {
        requestParams = RequestParams.create();
        requestParams.putString("caseID",id);
    }

    @Override
    public Observable<ChipGetInboxDetail> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.inbox_question_query), ChipInboxDetails.class, this.requestParams.getParameters(),false);
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(requestParams).map(new Func1<GraphqlResponse, ChipGetInboxDetail>() {
            @Override
            public ChipGetInboxDetail call(GraphqlResponse graphqlResponse) {
                ChipInboxDetails chipInboxDetails = ((ChipInboxDetails)(graphqlResponse.getData(ChipInboxDetails.class)));
                if(chipInboxDetails != null) {
                    return chipInboxDetails.getChipGetInboxDetail();
                }else {
                    return null;
                }
            }
        });
    }
}
