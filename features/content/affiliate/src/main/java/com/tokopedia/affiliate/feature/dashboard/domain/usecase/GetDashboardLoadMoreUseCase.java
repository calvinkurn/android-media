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
    private final static String PARAM_TYPE = "type";

    @Inject
    public GetDashboardLoadMoreUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(int type, String cursor) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_dashboard_loadmore),
                DashboardQuery.class, getParam(type, cursor).getParameters(),
                false);
    }

    public static RequestParams getParam(int type, String cursor) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_CURSOR, cursor);
        params.putInt(PARAM_TYPE, type);
        return params;
    }
}
