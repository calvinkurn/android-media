package com.tokopedia.affiliate.feature.dashboard.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

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
    private final UserSessionInterface userSession;

    private static final String PARAM_START_DATE = "startDate";
    private static final String PARAM_END_DATE = "endDate";
    private static final String PARAM_USER_ID = "userID";

    @Inject
    public GetAffiliateDashboardUseCase(@ApplicationContext Context context, UserSessionInterface userSession) {
        this.context = context;
        this.userSession = userSession;
    }

    public GraphqlRequest getRequest() {
        return getRequest(null, null);
    }

    public GraphqlRequest getRequest(Date startDate, Date endDate) {
        GraphqlRequest request = new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_affiliate_dashboard), DashboardQuery.class, false);
        request.setVariables(getRequestParams(startDate, endDate, Integer.valueOf(userSession.getUserId())).getParameters());
        return request;
    }

    private RequestParams getRequestParams(Date startDate, Date endDate, int userId) {
        RequestParams params = RequestParams.create();
        if (startDate != null) params.putString(PARAM_START_DATE, dateFormat.format(startDate));
        if (endDate != null) params.putString(PARAM_END_DATE, dateFormat.format(endDate));
        params.putInt(PARAM_USER_ID, userId);
        return params;
    }
}
