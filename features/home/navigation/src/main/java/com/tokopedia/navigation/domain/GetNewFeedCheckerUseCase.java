package com.tokopedia.navigation.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by yfsx on 15/11/18.
 */
public class GetNewFeedCheckerUseCase extends UseCase<RequestParams> {

    private static final String KEY_FEED = "KEY_FEED";
    private static final String KEY_FEED_FIRSTPAGE_LAST_CURSOR = "KEY_FEED_FIRSTPAGE_LAST_CURSOR";
    private static final String PARAM_FEED_LAST_CURSOR = "cursor";

    private Context context;

    public GetNewFeedCheckerUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Observable<RequestParams> createObservable(RequestParams requestParams) {
        return Observable.just(buildQueryParam(requestParams));
    }

    private RequestParams buildQueryParam(RequestParams requestParams) {
        LocalCacheHandler cache = new LocalCacheHandler(context.getApplicationContext(), KEY_FEED);
        requestParams.putString(PARAM_FEED_LAST_CURSOR,
                cache.getString(KEY_FEED_FIRSTPAGE_LAST_CURSOR , ""));
        return requestParams;
    }

}
