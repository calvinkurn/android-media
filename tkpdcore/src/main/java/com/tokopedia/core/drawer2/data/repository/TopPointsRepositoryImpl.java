package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.drawer2.data.factory.TopPointsSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;
import com.tokopedia.core.drawer2.domain.TopPointsRepository;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class TopPointsRepositoryImpl implements TopPointsRepository {

    private final TopPointsSourceFactory topPointsSourceFactory;

    public TopPointsRepositoryImpl(TopPointsSourceFactory topPointsSourceFactory) {
        this.topPointsSourceFactory = topPointsSourceFactory;
    }

    @Override
    public Observable<TopPointsModel> getTopPointsFromNetwork(TKPDMapParam<String, Object> params) {
        return topPointsSourceFactory.createCloudTopPointsSource().getTopPoints(params);
    }

    @Override
    public Observable<TopPointsModel> getTopPointsFromLocal() {
        return topPointsSourceFactory.createLocalTopPointsSource().getTopPoints();
    }
}
