package com.tokopedia.gm.common.data.source;

import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource;
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel;
import com.tokopedia.network.mapper.DataResponseMapper;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class GMCommonDataSource {
    private GMCommonCloudDataSource gmCommonCloudDataSource;

    @Inject
    public GMCommonDataSource(GMCommonCloudDataSource gmCommonCloudDataSource) {
        this.gmCommonCloudDataSource = gmCommonCloudDataSource;
    }

    public Observable<Boolean> setCashback(String productId, int cashback) {
        return gmCommonCloudDataSource.setCashback(new RequestCashbackModel(Long.parseLong(productId), cashback))
                .map(new DataResponseMapper<String>())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null;
                    }
                });
    }

}
