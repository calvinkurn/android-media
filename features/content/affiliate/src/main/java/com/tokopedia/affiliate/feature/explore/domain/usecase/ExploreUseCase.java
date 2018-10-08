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
    private final static String PARAM_QUERY = "query";

    @Inject
    public ExploreUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(String query, String cursor) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_dashboard_loadmore),
                ExploreQuery.class, getParam(query, cursor).getParameters());
    }

    public static RequestParams getParam(String query, String cursor) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_CURSOR, cursor);
        params.putString(PARAM_QUERY, query);
        return params;
    }
}
