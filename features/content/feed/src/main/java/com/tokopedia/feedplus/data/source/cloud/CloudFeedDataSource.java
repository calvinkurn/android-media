package com.tokopedia.feedplus.data.source.cloud;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.data.model.request.GraphqlRequest;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedplus.R;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.usecase.GetFeedsUseCase;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class CloudFeedDataSource {

    private static final String PARAM_USER_ID = "userID";
    private static final String PARAM_LIMIT = "limit";
    private static final String PARAM_CURSOR = "cursor";
    private static final String PARAM_SOURCE = "source";

    private static final int FEED_LIMIT = 3;
    private static final String FEED_SOURCE = "feeds";

    private final Context context;
    private final FeedApi feedApi;
    private final FeedListMapper feedListMapper;
    protected final CacheManager globalCacheManager;
    protected final FeedResultMapper feedResultMapper;

    public CloudFeedDataSource(@ApplicationContext Context context,
                               FeedApi feedApi,
                               FeedListMapper feedListMapper,
                               FeedResultMapper feedResultMapper,
                               CacheManager globalCacheManager) {
        this.context = context;
        this.feedApi = feedApi;
        this.feedListMapper = feedListMapper;
        this.globalCacheManager = globalCacheManager;
        this.feedResultMapper = feedResultMapper;
    }

    public Observable<FeedResult> getNextPageFeedsList(RequestParams requestParams) {
        return getFeedsList(requestParams).map(feedResultMapper);
    }

    protected Observable<FeedDomain> getFeedsList(RequestParams requestParams) {
        return feedApi.getFeedData(getRequestPayload(requestParams, R.raw.query_feed))
                .map(feedListMapper);
    }

    private GraphqlRequest getRequestPayload(RequestParams requestParams, int rawResourceId) {
        int userId = requestParams.getInt(GetFeedsUseCase.PARAM_USER_ID, 0);
        String cursor = requestParams.getString(GetFeedsUseCase.PARAM_CURSOR, "");

        HashMap<String, Object> params = new HashMap<>();
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_LIMIT, FEED_LIMIT);
        params.put(PARAM_CURSOR, cursor);
        params.put(PARAM_SOURCE, FEED_SOURCE);

        return new GraphqlRequest(
                loadRawString(context.getResources(), rawResourceId),
                params
        );
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
