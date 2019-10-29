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
public class GetCuratedProductListUseCase extends GraphqlUseCase {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final Context context;
    private final static String PARAM_CURSOR = "cursor";
    /**
     * Available type:
     * https://tokopedia.atlassian.net/wiki/spaces/CN/pages/544965123/GQL+Affiliate+Dashboard+-+AffiliatedProductList
     * 1 - Curated product from create post
     * 2 - Curated product from traffic
     * Leave it blank if you wanna show both data
     */
    private static final String PARAM_START_DATE = "startDate";
    private static final String PARAM_END_DATE = "endDate";
    private final static String PARAM_TYPE = "type";
    private final static String PARAM_SORT = "sort";

    @Inject
    public GetCuratedProductListUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(Integer type, String cursor, Integer sort) {
        return getRequest(type, cursor, sort, null, null);
    }

    public GraphqlRequest getRequest(Integer type, String cursor, Integer sort, Date startDate, Date endDate) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_curated_product_list),
                DashboardQuery.class, getParam(type, cursor, sort, startDate, endDate).getParameters(),
                false);
    }

    private RequestParams getParam(Integer type, String cursor, Integer sort, Date startDate, Date endDate) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_CURSOR, cursor);
        if (type != null) params.putInt(PARAM_TYPE, type);
        if (sort != null) params.putInt(PARAM_SORT, sort);
        if (startDate != null) params.putString(PARAM_START_DATE, dateFormat.format(startDate));
        if (endDate != null) params.putString(PARAM_END_DATE, dateFormat.format(endDate));
        return params;
    }
}
