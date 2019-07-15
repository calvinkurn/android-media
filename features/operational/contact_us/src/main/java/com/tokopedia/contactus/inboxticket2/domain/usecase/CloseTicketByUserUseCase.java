package com.tokopedia.contactus.inboxticket2.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail;
import com.tokopedia.contactus.inboxticket2.data.model.ChipInboxDetails;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class CloseTicketByUserUseCase extends UseCase<ChipGetInboxDetail> {
    private Context context;
    private GraphqlUseCase graphqlUseCase;
    private static final String CASEID = "caseID" ;
    private static final String SOURCE = "source" ;

    @Inject
    public CloseTicketByUserUseCase(Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public RequestParams createRequestParams(String caseID, String source) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CASEID,caseID);
        requestParams.putString(SOURCE, source);
        return requestParams;
    }

    @Override
    public Observable<ChipGetInboxDetail> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.close_ticket_by_user), ChipInboxDetails.class, requestParams.getParameters(),false);
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(requestParams).map(new Func1<GraphqlResponse, ChipGetInboxDetail>() {
            @Override
            public ChipGetInboxDetail call(GraphqlResponse graphqlResponse) {
                return ((ChipInboxDetails)(graphqlResponse.getData(ChipInboxDetails.class))).getChipGetInboxDetail();
            }
        });
    }
}
