package com.tokopedia.kolcommon.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.GraphqlConstant;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kolcommon.R;
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by yfsx on 20/06/18.
 */
public class GetWhitelistUseCase extends GraphqlUseCase {

    private final Context context;
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_ID = "ID";
    public static final String WHITELIST_CONTENT_USER = "content-user";
    public static final String WHITELIST_SHOP = "content-shop";
    public static final String WHITELIST_INTEREST = "interest";

    @Inject
    public GetWhitelistUseCase(@ApplicationContext Context context) {
        this.context = context;
        this.setCacheStrategy(
                new GraphqlCacheStrategy
                        .Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.val())
                        .setSessionIncluded(true)
                        .build()
        );
    }

    public GraphqlRequest getRequest(HashMap<String, Object> variables) {
        return new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_whitelist),
                WhitelistQuery.class,
                variables,
                false
        );
    }

    public void execute(HashMap<String, Object> variables,
                        Subscriber<GraphqlResponse> subscriber) {
        GraphqlRequest graphqlRequest = new GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.query_whitelist),
                WhitelistQuery.class,
                variables,
                false
        );
        this.clearRequest();
        this.addRequest(graphqlRequest);
        this.execute(subscriber);
    }

    public static HashMap<String, Object> createRequestParams(String type) {
        return createRequestParams(type, "");
    }

    public static HashMap<String, Object> createRequestParams(String type, String id) {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put(PARAM_TYPE, type);
        variables.put(PARAM_ID, id);
        return variables;
    }
}
