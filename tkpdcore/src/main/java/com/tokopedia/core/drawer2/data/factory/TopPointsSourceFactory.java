package com.tokopedia.core.drawer2.data.factory;

import android.content.Context;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.mapper.TopPointsMapper;
import com.tokopedia.core.drawer2.data.source.CloudTopPointsSource;
import com.tokopedia.core.drawer2.data.source.LocalTopPointsSource;
import com.tokopedia.core.network.apiservices.clover.CloverService;

/**
 * Created by nisie on 5/5/17.
 */

public class TopPointsSourceFactory {

    public static final String KEY_TOPPOINTS_DATA = "KEY_TOPPOINTS_DATA";

    private Context context;
    private CloverService cloverService;
    private TopPointsMapper topPointsMapper;
    private GlobalCacheManager topPointsCache;

    public TopPointsSourceFactory(Context context,
                                 CloverService cloverService,
                                 TopPointsMapper topPointsMapper,
                                 GlobalCacheManager topPointsCache) {
        this.context = context;
        this.cloverService = cloverService;
        this.topPointsMapper = topPointsMapper;
        this.topPointsCache = topPointsCache;
    }

    public CloudTopPointsSource createCloudTopPointsSource() {
        return new CloudTopPointsSource(context, cloverService,
                topPointsMapper, topPointsCache);
    }

    public LocalTopPointsSource createLocalTopPointsSource() {
        return new LocalTopPointsSource(topPointsCache);
    }
}
