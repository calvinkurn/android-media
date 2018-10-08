package com.tokopedia.affiliate.feature.explore.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreQuery;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
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

    public GraphqlRequest getRequest(ExploreParams exploreParams) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_dashboard_loadmore),
                ExploreQuery.class, getParam(exploreParams).getParameters());
    }

    public static RequestParams getParam(ExploreParams exploreParams) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_KEYWORD, exploreParams.getKeyword());
        params.putString(PARAM_CURSOR, exploreParams.getCursor());
        params.putString(PARAM_FILTER_KEY, exploreParams.getFilterKey());
        params.putString(PARAM_FILTER_VALUE, exploreParams.getFilterValue());
        params.putString(PARAM_SORT_KEY, exploreParams.getSortKey());
        params.putBoolean(PARAM_SORT_ASC, exploreParams.isSortAsc());
        return params;
    }
}
