package com.tokopedia.topads.dashboard.data.repository;

import com.tokopedia.topads.dashboard.data.factory.TopAdsShopAdFactory;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsShopAdsDataSource;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */

public class TopAdsShopAdsRepositoryImpl implements TopAdsShopAdsRepository {

    private final TopAdsShopAdFactory topAdsShopAdFactory;

    public TopAdsShopAdsRepositoryImpl(TopAdsShopAdFactory topAdsShopAdFactory) {
        this.topAdsShopAdFactory = topAdsShopAdFactory;
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> getDetail(String adId) {
        TopAdsShopAdsDataSource topAdsShopAdsDataSource = topAdsShopAdFactory.createShopAdsDataSource();
        return topAdsShopAdsDataSource.getDetailProduct(adId);
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> saveDetail(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {
        TopAdsShopAdsDataSource topAdsShopAdsDataSource = topAdsShopAdFactory.createShopAdsDataSource();
        return topAdsShopAdsDataSource.saveDetailProduct(topAdsDetailShopDomainModel);
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> createDetailShop(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {
        TopAdsShopAdsDataSource topAdsShopAdsDataSource = topAdsShopAdFactory.createShopAdsDataSource();
        return topAdsShopAdsDataSource.createDetailShop(topAdsDetailShopDomainModel);
    }


}
