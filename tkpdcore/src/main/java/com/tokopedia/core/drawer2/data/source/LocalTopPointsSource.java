package com.tokopedia.core.drawer2.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.TopPointsSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 5/5/17.
 */

public class LocalTopPointsSource {
    private final GlobalCacheManager topPointsCache;

    public LocalTopPointsSource(GlobalCacheManager topPointsCache) {
        this.topPointsCache = topPointsCache;
    }


    public Observable<TopPointsModel> getTopPoints() {
        return Observable.just(true).map(new Func1<Boolean, TopPointsModel>() {
            @Override
            public TopPointsModel call(Boolean aBoolean) {

                if (getCache() != null) {
                    return CacheUtil.convertStringToModel(getCache(),
                            new TypeToken<TopPointsModel>() {
                            }.getType());
                } else
                    throw new RuntimeException("Cache has expired");

            }
        });
    }

    private String getCache() {
        return topPointsCache.getValueString(TopPointsSourceFactory.KEY_TOPPOINTS_DATA);
    }
}
