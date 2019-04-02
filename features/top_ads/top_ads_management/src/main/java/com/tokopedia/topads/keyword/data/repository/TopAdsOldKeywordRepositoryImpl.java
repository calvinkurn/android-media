package com.tokopedia.topads.keyword.data.repository;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.product.manage.item.common.domain.model.AddProductShopInfoDomainModel;
import com.tokopedia.product.manage.item.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.keyword.data.source.KeywordDashboardDataSouce;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class TopAdsOldKeywordRepositoryImpl implements TopAdsKeywordRepository {

    private KeywordDashboardDataSouce keywordDashboardDataSouce;
    private ShopInfoRepository shopInfoRepository;

    @Inject
    public TopAdsOldKeywordRepositoryImpl(KeywordDashboardDataSouce keywordDashboardDataSouce, ShopInfoRepository shopInfoRepository) {
        this.keywordDashboardDataSouce = keywordDashboardDataSouce;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<KeywordDashboardDomain> getDashboardKeyword(final RequestParams requestParams) {
        return shopInfoRepository.getAddProductShopInfo().flatMap(new Func1<AddProductShopInfoDomainModel, Observable<KeywordDashboardDomain>>() {
            @Override
            public Observable<KeywordDashboardDomain> call(AddProductShopInfoDomainModel addProductShopInfoDomainModel) {
                requestParams.putString("shop_id", addProductShopInfoDomainModel.getShopId());
                return keywordDashboardDataSouce.getKeywordDashboard(requestParams);
            }
        });
    }

    @Override
    public Observable<EditTopAdsKeywordDetailDomainModel> editTopAdsKeywordDetail(TopAdsKeywordEditDetailInputDomainModel modelInput) {
        modelInput.setShopId(shopInfoRepository.getShopId());
        return keywordDashboardDataSouce.editTopAdsKeywordDetail(modelInput);
    }

    @Override
    public Observable<AddKeywordDomainModel> addKeyword(final AddKeywordDomainModel addKeywordDomainModel) {
        return keywordDashboardDataSouce.addKeyword(addKeywordDomainModel);
    }
}
