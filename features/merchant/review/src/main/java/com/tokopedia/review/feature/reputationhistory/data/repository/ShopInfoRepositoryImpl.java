package com.tokopedia.review.feature.reputationhistory.data.repository;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.review.feature.reputationhistory.data.mapper.ShopInfoDataToDomainMapper;
import com.tokopedia.review.feature.reputationhistory.data.source.ShopInfoDataSource;
import com.tokopedia.review.feature.reputationhistory.domain.interactor.AddProductShopInfoDomainModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/20/17.
 */

public class ShopInfoRepositoryImpl implements ShopInfoRepository {
    private final ShopInfoDataSource shopInfoDataSource;
    private Context context;

    @Inject
    public ShopInfoRepositoryImpl(@ApplicationContext  Context context, ShopInfoDataSource shopInfoDataSource) {
        this.shopInfoDataSource = shopInfoDataSource;
        this.context = context;
    }

    @Override
    public Observable<AddProductShopInfoDomainModel> getAddProductShopInfo() {
        return shopInfoDataSource.getShopInfo().map(new ShopInfoDataToDomainMapper());
    }

    @Override
    public Observable<ShopModel> getShopInfo() {
        return shopInfoDataSource.getShopInfo();
    }

    @Override
    public String getShopId() {
        UserSessionInterface userSession = new UserSession(context);
        return userSession.getShopId();
    }

}
