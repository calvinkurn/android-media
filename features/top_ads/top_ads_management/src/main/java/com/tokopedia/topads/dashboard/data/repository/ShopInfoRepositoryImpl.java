package com.tokopedia.topads.dashboard.data.repository;

import android.content.Context;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.product.manage.item.common.data.mapper.ShopInfoDataToDomainMapper;
import com.tokopedia.product.manage.item.common.domain.model.AddProductShopInfoDomainModel;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.dashboard.data.source.ShopInfoDataSource;

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
        return SessionHandler.getShopID(context);
    }

    @Override
    public Observable<ShopModel> getShopInfo() {
        return  shopInfoDataSource.getShopInfo();
    }
}
