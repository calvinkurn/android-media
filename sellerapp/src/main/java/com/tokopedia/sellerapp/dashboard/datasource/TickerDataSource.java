package com.tokopedia.sellerapp.dashboard.datasource;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.edit.data.source.cache.ShopInfoCache;
import com.tokopedia.seller.product.edit.data.source.cloud.ShopInfoCloud;
import com.tokopedia.sellerapp.home.model.Ticker;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by User on 9/11/2017.
 */

public class TickerDataSource {
    private final TickerCloud tickerCloud;

    @Inject
    public TickerDataSource(TickerCloud tickerCloud) {
        this.tickerCloud = tickerCloud;
    }

    public Observable<Response<Ticker>> getTicker(String userId) {
        return tickerCloud.getTicker(userId);
    }
}
