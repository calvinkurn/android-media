package com.tokopedia.topads.dashboard.data.source;

import com.tokopedia.network.mapper.DataResponseMapper;
import com.tokopedia.topads.common.model.shopmodel.ShopModel;
import com.tokopedia.topads.dashboard.data.source.cloud.ShopInfoCloud;

import rx.Observable;

/**
 * Created by hadi.putra on 03/05/18.
 */

public class ShopInfoDataSource {
    private final ShopInfoCloud shopInfoCloud;
    private final DataResponseMapper<ShopModel> mapper;

    public ShopInfoDataSource(ShopInfoCloud shopInfoCloud, DataResponseMapper<ShopModel> mapper) {
        this.shopInfoCloud = shopInfoCloud;
        this.mapper = mapper;
    }

    public Observable<ShopModel> getShopInfo() {
        return shopInfoCloud.getShopInfo()
                .map(mapper);
    }
}
