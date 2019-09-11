package com.tokopedia.affiliate.feature.dashboard.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * @author by yfsx on 19/09/18.
 */
public class GetAffiliateDashboardUseCase extends GraphqlUseCase {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private final Context context;

    @Inject
    public GetAffiliateDashboardUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest() {
        return getRequest(null, null);
    }

    public GraphqlRequest getRequest(Date startDate, Date endDate) {
        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_affiliate_dashboard), DashboardQuery.class, false);
        request.setVariables(getRequestParams(startDate, endDate).getParameters());
        return request;
    }

    private RequestParams getRequestParams(Date startDate, Date endDate) {
        RequestParams params = RequestParams.create();
        if (startDate != null) params.putString("startDate", dateFormat.format(startDate));
        if (endDate != null) params.putString("endDate", dateFormat.format(endDate));
        return params;
    }
}
