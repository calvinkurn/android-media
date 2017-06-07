package com.tokopedia.core.drawer2.data.source;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.TopPointsSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.TopPointsMapper;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 5/5/17.
 */

public class CloudTopPointsSource {

    private final Context context;
    private final CloverService cloverService;
    private final TopPointsMapper topPointsMapper;
    private final GlobalCacheManager topPointsCache;

    public CloudTopPointsSource(Context context,
                                CloverService cloverService,
                                TopPointsMapper topPointsMapper,
                                GlobalCacheManager topPointsCache) {
        this.context = context;
        this.cloverService = cloverService;
        this.topPointsCache = topPointsCache;
        this.topPointsMapper = topPointsMapper;
    }

    public Observable<TopPointsModel> getTopPoints(TKPDMapParam<String, Object> params) {
        return cloverService.getApi()
                .getTopPoints2(AuthUtil.generateParamsNetwork2(context, params))
                .map(topPointsMapper)
                .doOnNext(saveToCache());
    }

    private Action1<TopPointsModel> saveToCache() {
        return new Action1<TopPointsModel>() {
            @Override
            public void call(TopPointsModel topPointsModel) {
                if (topPointsModel != null && topPointsModel.isSuccess()) {
                    topPointsCache.setKey(TopPointsSourceFactory.KEY_TOPPOINTS_DATA);
                    topPointsCache.setValue(CacheUtil.convertModelToString(topPointsModel,
                            new TypeToken<TopPointsModel>() {
                            }.getType()));
                    topPointsCache.setCacheDuration(9000);
                    topPointsCache.store();
                }
            }
        };
    }
}
