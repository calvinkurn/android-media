package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashRepositoryImpl implements TokoCashRepository {

    private final TokoCashSourceFactory tokoCashSourceFactory;

    public TokoCashRepositoryImpl(TokoCashSourceFactory tokoCashSourceFactory) {
        this.tokoCashSourceFactory = tokoCashSourceFactory;
    }

    @Override
    public Observable<TokoCashModel> getTokoCashFromNetwork(TKPDMapParam<String, Object> params) {
        return tokoCashSourceFactory.createCloudTokoCashSource().getTokoCash(params);
    }

    @Override
    public Observable<TokoCashModel> getTokoCashFromLocal() {
        return tokoCashSourceFactory.createLocalTokoCashSource().getTokoCash();
    }
}
