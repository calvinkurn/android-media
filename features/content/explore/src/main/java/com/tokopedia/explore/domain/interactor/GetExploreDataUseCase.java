package com.tokopedia.explore.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.explore.R;
import com.tokopedia.explore.domain.entity.GetExploreData;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by milhamj on 23/07/18.
 */

public class GetExploreDataUseCase {

    private static final String PARAM_CATEGORY_ID = "idcategory";
    private static final String PARAM_CURSOR = "cursor";
    private static final String PARAM_SEARCH = "search";
    private static final String PARAM_LIMIT = "limit";
    private static final int LIMIT = 18;

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    GetExploreDataUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.graphqlUseCase.setCacheStrategy(
                new GraphqlCacheStrategy
                        .Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.val())
                        .setSessionIncluded(true)
                        .build()
        );
    }

    public void execute(Map<String, Object> variables,
                        Subscriber<GraphqlResponse> getExploreDataSubscriber) {
        String query = GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_get_explore_data);

        GraphqlRequest request = new GraphqlRequest(query, GetExploreData.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(request);
        graphqlUseCase.execute(getExploreDataSubscriber);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            graphqlUseCase.unsubscribe();
        }
    }

    public static Map<String, Object> getVariables(int categoryId, String cursor, String search) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(PARAM_CATEGORY_ID, categoryId);
        variables.put(PARAM_CURSOR, cursor);
        variables.put(PARAM_SEARCH, search);
        variables.put(PARAM_LIMIT, LIMIT);
        return variables;
    }
}
