package com.tokopedia.tkpd.home.feed.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.common.service.AceService;
import com.tokopedia.core.database.model.DbFeed;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.feed.data.mapper.FeedMapper;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.FeedDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 12/9/16.
 */
public class CloudFeedDataStore {
    private static final String KEY_PARAMS_USER_ID = "user_id";

    private Context context;
    private AceService aceService;
    private FeedMapper feedMapper;
    private FeedDbManager feedDbManager;

    public CloudFeedDataStore(Context context, AceService aceService,
                              FeedMapper feedMapper, FeedDbManager feedDbManager) {
        super();

        this.context = context;
        this.aceService = aceService;
        this.feedMapper = feedMapper;
        this.feedDbManager = feedDbManager;
    }

    public Observable<Feed> getFeed(TKPDMapParam<String, String> params) {
        params.put(KEY_PARAMS_USER_ID, SessionHandler.getLoginID(context));
        return aceService.getProductFeed(params).doOnNext(saveToCache()).map(feedMapper);
    }

    private Action1<Response<String>> saveToCache() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                if (stringResponse.isSuccessful() && stringResponse.body() != null) {
                    DbFeed dbFeed = new DbFeed();
                    dbFeed.setId(1); //set 1 in order to force replace
                    dbFeed.setLastUpdated(System.currentTimeMillis());
                    dbFeed.setContentFeed(stringResponse.body());
                    feedDbManager.store(dbFeed);
                }
            }
        };
    }

}
