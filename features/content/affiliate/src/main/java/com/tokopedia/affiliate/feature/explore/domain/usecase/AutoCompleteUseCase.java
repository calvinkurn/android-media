package com.tokopedia.affiliate.feature.explore.domain.usecase;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.data.pojo.AutoCompleteQuery;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * @author by yfsx on 24/10/18.
 */
public class AutoCompleteUseCase extends GraphqlUseCase {

    private final static String PARAM_KEYWORD = "keyword";
    private final static String PARAM_LIMIT = "limit";
    private final static int COUNT_LIMIT = 3;

    private final Context context;

    @Inject
    public AutoCompleteUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(String keyword) {
        return new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_explore_auto_complete),
                AutoCompleteQuery.class,
                getParam(keyword).getParameters(), false
        );
    }

    public static RequestParams getParam(String keyword) {
        RequestParams params = RequestParams.create();
        if (!TextUtils.isEmpty(keyword)) {
            params.putString(PARAM_KEYWORD, keyword);
            params.putInt(PARAM_LIMIT, COUNT_LIMIT);
        }
        return params;
    }
}
