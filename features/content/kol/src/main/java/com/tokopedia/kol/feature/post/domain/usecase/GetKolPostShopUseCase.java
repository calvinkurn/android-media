package com.tokopedia.kol.feature.post.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.R;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by milhamj on 23/08/18.
 */

public class GetKolPostShopUseCase {

    private static final String PARAM_LIMIT = "limit";
    private static final String PARAM_SHOP_ID = "shopID";
    private static final String PARAM_CURSOR = "cursor";
    private static final String PARAM_SOURCE = "source";
    private static final int DEFAULT_LIMIT = 5;
    private static final String DEFAULT_SOURCE = "feeds";

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    GetKolPostShopUseCase(@ApplicationContext Context context,
                          GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }


    public void execute(RequestParams requestParams, Subscriber<GraphqlResponse> subscriber) {
        graphqlUseCase.clearRequest();

        String query = GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_content_list);

        Map<String, Object> variables = requestParams.getParameters();

        //TODO milhamj change response class.
        GraphqlRequest feedDetailGraphqlRequest =
                new GraphqlRequest(query,
                        GetKolPostShopUseCase.class,
                        variables);

        graphqlUseCase.addRequest(feedDetailGraphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public static RequestParams getParams(String shopId, String cursor) {
        RequestParams params = RequestParams.create();
        params.putInt(GetKolPostShopUseCase.PARAM_SHOP_ID, Integer.valueOf(shopId));
        params.putString(GetKolPostShopUseCase.PARAM_CURSOR, cursor);
        params.putInt(GetKolPostShopUseCase.PARAM_LIMIT, DEFAULT_LIMIT);
        params.putString(GetKolPostShopUseCase.PARAM_SOURCE, DEFAULT_SOURCE);
        return params;
    }
}
