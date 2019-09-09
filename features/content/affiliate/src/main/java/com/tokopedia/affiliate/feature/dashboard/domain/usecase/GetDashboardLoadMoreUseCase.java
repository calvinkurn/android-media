package com.tokopedia.affiliate.feature.dashboard.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * @author by yfsx on 19/09/18.
 */
public class GetDashboardLoadMoreUseCase extends GraphqlUseCase {
    private final Context context;
    private final static String PARAM_CURSOR = "cursor";
    /**
     * Available type:
     * https://tokopedia.atlassian.net/wiki/spaces/CN/pages/544965123/GQL+Affiliate+Dashboard+-+AffiliatedProductList
     * 1 - Curated product from create post
     * 2 - Curated product from traffic
     * Leave it blank if you wanna show both data
     */
    private final static String PARAM_TYPE = "type";
    private final static String PARAM_SORT = "sort";

    @Inject
    public GetDashboardLoadMoreUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(Integer type, String cursor, int sort) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_dashboard_loadmore),
                DashboardQuery.class, getParam(type, cursor, sort).getParameters(),
                false);
    }

    public static RequestParams getParam(Integer type, String cursor, int sort) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_CURSOR, cursor);
        if (type != null) params.putInt(PARAM_TYPE, type);
        params.putInt(PARAM_SORT, sort);
        return params;
    }
}
