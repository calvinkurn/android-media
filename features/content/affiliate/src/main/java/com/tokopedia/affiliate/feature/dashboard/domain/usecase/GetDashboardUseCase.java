package com.tokopedia.affiliate.feature.dashboard.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import javax.inject.Inject;

/**
 * @author by yfsx on 19/09/18.
 */
public class GetDashboardUseCase extends GraphqlUseCase {
    private final Context context;

    @Inject
    public GetDashboardUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest() {
        return new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_dashboard), DashboardQuery.class, false);
    }
}
