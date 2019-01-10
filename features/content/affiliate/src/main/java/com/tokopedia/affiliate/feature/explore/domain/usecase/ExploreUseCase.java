package com.tokopedia.affiliate.feature.explore.domain.usecase;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreData;
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
    private final static String PARAM_CURSOR = "nextCursor";
    private final static String PARAM_KEYWORD = "keyword";
    private final static String PARAM_FILTER= "filter";
    private final static String PARAM_SORT = "sort";

    @Inject
    public ExploreUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(ExploreParams exploreParams) {
        String query = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_explore
        );
        return new GraphqlRequest(
                query,
                ExploreData.class,
                getParam(exploreParams).getParameters()
        );
    }

    public GraphqlRequest getRequestLoadMore(ExploreParams exploreParams) {
        String query = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_explore_load_more
        );
        return new GraphqlRequest(
                query,
                ExploreData.class,
                getParam(exploreParams).getParameters()
        );
    }

    public static RequestParams getParam(ExploreParams exploreParams) {
        RequestParams params = RequestParams.create();
        if (!TextUtils.isEmpty(exploreParams.getKeyword())) {
            params.putString(PARAM_KEYWORD, exploreParams.getKeyword());
        }
        if (!TextUtils.isEmpty(exploreParams.getCursor())) {
            params.putString(PARAM_CURSOR, exploreParams.getCursor());
        }
        if (exploreParams.getFilters().size() != 0) {
            params.putObject(PARAM_FILTER, exploreParams.getFilters());
        }
//        if (!TextUtils.isEmpty(exploreParams.getSort().getKey())) {
//            params.putObject(PARAM_SORT, exploreParams.getSort());
//        }
        return params;
    }
}
