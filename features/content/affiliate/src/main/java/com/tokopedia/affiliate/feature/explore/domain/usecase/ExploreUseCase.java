package com.tokopedia.affiliate.feature.explore.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreQuery;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreUseCase extends GraphqlUseCase {
    private final Context context;
    private final static String PARAM_CURSOR = "cursor";
    private final static String PARAM_KEYWORD = "keyword";
    private final static String PARAM_FILTER_KEY = "filterKey";
    private final static String PARAM_FILTER_VALUE = "filterValue";
    private final static String PARAM_SORT_KEY = "sortKey";
    private final static String PARAM_SORT_ASC = "SortAsc";

    @Inject
    public ExploreUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(String query,
                                     String cursor,
                                     String filterKey,
                                     String filterValue,
                                     String sortKey,
                                     boolean sortAsc) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_dashboard_loadmore),
                ExploreQuery.class, getParam(query, cursor, filterKey, filterValue, sortKey, sortAsc).getParameters());
    }

    public static RequestParams getParam(String query,
                                         String cursor,
                                         String filterKey,
                                         String filterValue,
                                         String sortKey,
                                         boolean sortAsc) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_KEYWORD, query);
        params.putString(PARAM_CURSOR, cursor);
        params.putString(PARAM_FILTER_KEY, filterKey);
        params.putString(PARAM_FILTER_VALUE, filterValue);
        params.putString(PARAM_SORT_KEY, sortKey);
        params.putBoolean(PARAM_SORT_ASC, sortAsc);
        return params;
    }
}
