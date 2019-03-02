package com.tokopedia.kol.feature.post.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.post.data.pojo.FollowKolQuery;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * @author by yfsx on 11/7/18.
 */

public class FollowKolPostGqlUseCase extends GraphqlUseCase {

    private static final String PARAM_USER_ID = "userID";
    public static final String PARAM_ACTION = "action";
    public static final int PARAM_FOLLOW = 1;
    public static final int PARAM_UNFOLLOW = 0;
    public static final int SUCCESS_STATUS = 1;

    private final Context context;

    @Inject
    public FollowKolPostGqlUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(int userId, int status) {
        return new GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.query_follow_kol), FollowKolQuery.class, getParam(userId, status).getParameters(), false);
    }

    public static RequestParams getParam(int userId, int status) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_USER_ID, userId);
        params.putInt(PARAM_ACTION, status);
        return params;
    }
}
