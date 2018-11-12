package com.tokopedia.kol.feature.post.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.R;
import com.tokopedia.kol.feature.post.data.mapper.GetContentListMapper;
import com.tokopedia.kol.feature.post.data.pojo.shop.ContentListData;
import com.tokopedia.kol.feature.post.domain.model.ContentListDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by milhamj on 23/08/18.
 */

public class GetContentListUseCase extends UseCase<ContentListDomain> {

    private static final String PARAM_LIMIT = "limit";
    private static final String PARAM_SHOP_ID = "shopID";
    private static final String PARAM_USER_ID = "userID";
    private static final String PARAM_CURSOR = "cursor";
    private static final String PARAM_SOURCE = "source";

    private static final int LIMIT_5 = 5;
    private static final String SOURCE_FEEDS = "feeds";
    private static final String SOURCE_PROFILE = "profile";

    private final Context context;
    private final GraphqlUseCase graphqlUseCase;
    private final GetContentListMapper getContentListMapper;

    @Inject
    GetContentListUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase,
                          GetContentListMapper getContentListMapper) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
        this.getContentListMapper = getContentListMapper;
    }

    @Override
    public Observable<ContentListDomain> createObservable(RequestParams params) {
        String query = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_content_list
        );
        Map<String, Object> variables = params.getParameters();
        GraphqlRequest request = new GraphqlRequest(query, ContentListData.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(request);
        return graphqlUseCase.createObservable(params).flatMap(getContentListMapper);
    }

    public static RequestParams getShopParams(String shopId, String cursor) {
        RequestParams params = RequestParams.create();
        params.putInt(GetContentListUseCase.PARAM_LIMIT, LIMIT_5);
        params.putInt(GetContentListUseCase.PARAM_SHOP_ID, Integer.valueOf(shopId));
        params.putString(GetContentListUseCase.PARAM_CURSOR, cursor);
        params.putString(GetContentListUseCase.PARAM_SOURCE, SOURCE_FEEDS);
        return params;
    }

    public static RequestParams getProfileParams(Integer userId, String cursor) {
        RequestParams params = RequestParams.create();
        params.putInt(GetContentListUseCase.PARAM_USER_ID, userId);
        params.putString(GetContentListUseCase.PARAM_CURSOR, cursor);
        params.putString(GetContentListUseCase.PARAM_SOURCE, SOURCE_PROFILE);
        return params;
    }
}
