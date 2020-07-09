package com.tokopedia.kol.feature.postdetail.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetKolCommentData;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

import static com.tokopedia.kol.feature.comment.data.raw.GqlQueryGetKolCommentKt.GQL_QUERY_GET_KOL_COMMENT;

/**
 * @author by milhamj on 27/07/18.
 */

public class GetKolPostDetailUseCase {

    public static final String PARAM_ID = "idPost";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";
    public static final String FIRST_CURSOR = "";

    public static final int DEFAULT_LIMIT = 3;

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    public GetKolPostDetailUseCase(@ApplicationContext Context context,
                                   GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void execute(Map<String, Object> variables, Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();

        GraphqlRequest request = new GraphqlRequest(GQL_QUERY_GET_KOL_COMMENT, GetKolCommentData.class, variables);

        graphqlUseCase.addRequest(request);
        graphqlUseCase.execute(subscriber);
    }

    public Observable<GraphqlResponse> createObservable(RequestParams requestParams
                                                        ) {
        graphqlUseCase.clearRequest();

        GraphqlRequest request = new GraphqlRequest(GQL_QUERY_GET_KOL_COMMENT, GetKolCommentData.class,
                requestParams.getParameters());

        graphqlUseCase.addRequest(request);
       return graphqlUseCase.createObservable(requestParams);
    }

    public void unsubsribe() {
        graphqlUseCase.unsubscribe();
    }

    public static Map<String, Object> getVariables(int postId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(PARAM_ID, postId);
        variables.put(PARAM_CURSOR, FIRST_CURSOR);
        variables.put(PARAM_LIMIT, DEFAULT_LIMIT);
        return variables;
    }
}
