package com.tokopedia.topads.dashboard.data.repository;

import android.content.Context;

import com.tokopedia.topads.common.model.AddProductShopInfoDomainModel;
import com.tokopedia.topads.common.model.shopmodel.ShopModel;
import com.tokopedia.topads.dashboard.data.mapper.ShopInfoDataToDomainMapper;
import com.tokopedia.topads.dashboard.data.source.ShopInfoDataSource;
import com.tokopedia.user.session.UserSession;

import rx.Observable;

/**
 * Created by hadi.putra on 03/05/18.
 */

public class ShopInfoRepositoryImpl implements ShopInfoRepository {
    private final ShopInfoDataSource shopInfoDataSource;
    private Context context;

    public ShopInfoRepositoryImpl(Context context, ShopInfoDataSource shopInfoDataSource) {
        this.shopInfoDataSource = shopInfoDataSource;
        this.context = context;
    }

    @Override
    public Observable<AddProductShopInfoDomainModel> getAddProductShopInfo() {
        return shopInfoDataSource.getShopInfo().map(new ShopInfoDataToDomainMapper());
    }

    @Override
    public String getShopId() {
        return new UserSession(context).getShopId();
    }

    @Override
    public Observable<ShopModel> getShopInfo() {
        return  shopInfoDataSource.getShopInfo();
    }
}
