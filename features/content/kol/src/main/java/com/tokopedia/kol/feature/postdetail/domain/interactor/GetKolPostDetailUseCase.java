package com.tokopedia.kol.feature.postdetail.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.comment.data.pojo.get.GetKolCommentData;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by milhamj on 27/07/18.
 */

public class GetKolPostDetailUseCase {

    private static final String PARAM_ID = "idPost";
    private static final String PARAM_CURSOR = "cursor";
    private static final String PARAM_LIMIT = "limit";
    private static final String FIRST_CURSOR = "";

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

        String query = GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_get_kol_comment);

        GraphqlRequest request = new GraphqlRequest(query, GetKolCommentData.class, variables, false);

        graphqlUseCase.addRequest(request);
        graphqlUseCase.execute(subscriber);
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
