package com.tokopedia.explore.domain.interactor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.explore.R;
import com.tokopedia.explore.domain.entity.GetDiscoveryKolData;
import com.tokopedia.explore.view.subscriber.GetExploreDataSubscriber;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author by milhamj on 23/07/18.
 */

public class GetExploreDataUseCase {

    private static final String PARAM_CATEGORY_ID = "idcategory";
    private static final String PARAM_CURSOR = "cursor";
    private static final String PARAM_SEARCH = "search";
    private static final String PARAM_LIMIT = "limit";
    private static final int LIMIT = 9;

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    GetExploreDataUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    public void execute(Map<String, Object> variables,
                        GetExploreDataSubscriber getExploreDataSubscriber) {
        graphqlUseCase.clearRequest();

        String query = GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_get_explore_data);

        GraphqlRequest request = new GraphqlRequest(query, GetDiscoveryKolData.class, variables);

        graphqlUseCase.addRequest(request);
        graphqlUseCase.execute(getExploreDataSubscriber);
    }

    public void unsubscribe() {
        if (graphqlUseCase != null) {
            unsubscribe();
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
