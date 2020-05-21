package com.tokopedia.contactus.inboxticket2.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.data.model.TicketReplyResponse;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;


public class PostMessageUseCase extends UseCase<TicketReplyResponse.TicketReply> {

    Context context;
    GraphqlUseCase graphqlUseCase;
    UserSessionInterface userSession;
    RequestParams requestParams;

    @Inject
    PostMessageUseCase(Context context, GraphqlUseCase graphqlUseCase, UserSessionInterface userSession) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.userSession = userSession;
    }

    @Override
    public Observable<TicketReplyResponse.TicketReply> createObservable(RequestParams requestParams) {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.reply_ticket_query), TicketReplyResponse.class, this.requestParams.getParameters(), false);
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(requestParams).map(new Func1<GraphqlResponse, TicketReplyResponse.TicketReply>() {
            @Override
            public TicketReplyResponse.TicketReply call(GraphqlResponse graphqlResponse) {
                TicketReplyResponse ticketReplyResponse = ((TicketReplyResponse) (graphqlResponse.getData(TicketReplyResponse.class)));
                if (ticketReplyResponse != null) {
                    return ticketReplyResponse.getTicketReply();
                } else {
                    return null;
                }
            }
        });
    }


    public void createRequestParams(String id, String message, int photo, String photoall, String agentReply) {
        requestParams = RequestParams.create();
        requestParams.putString("ticketID", id);
        requestParams.putString("message", message);
        requestParams.putString("agentReply", agentReply);
        requestParams.putString("userID", userSession.getUserId());
        requestParams.putInt("pPhoto", photo);
        requestParams.putString("pPhotoAll", photoall);
    }
}
