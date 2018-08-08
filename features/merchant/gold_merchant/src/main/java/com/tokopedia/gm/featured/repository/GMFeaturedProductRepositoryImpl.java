package com.tokopedia.gm.featured.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.gm.featured.data.GMFeaturedProductDataSource;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductSubmitModel;
import com.tokopedia.gm.featured.domain.interactor.GMFeaturedProductSubmitUseCase;
import com.tokopedia.gm.featured.domain.mapper.GMFeaturedProductMapper;
import com.tokopedia.gm.featured.domain.mapper.GMFeaturedProductSubmitMapper;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.gm.featured.domain.model.GMFeaturedProductSubmitDomainModel;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductRepositoryImpl implements GMFeaturedProductRepository {

    private GMFeaturedProductDataSource GMFeaturedProductDataSource;
    private ShopInfoRepository shopInfoRepository;
    private GMFeaturedProductMapper GMFeaturedProductMapper;
    private GMFeaturedProductSubmitMapper GMFeaturedProductSubmitMapper;

    public GMFeaturedProductRepositoryImpl(GMFeaturedProductDataSource GMFeaturedProductDataSource,
                                           ShopInfoRepository shopInfoRepository,
                                           GMFeaturedProductMapper GMFeaturedProductMapper,
                                           GMFeaturedProductSubmitMapper GMFeaturedProductSubmitMapper) {
        this.GMFeaturedProductDataSource = GMFeaturedProductDataSource;
        this.shopInfoRepository = shopInfoRepository;
        this.GMFeaturedProductMapper = GMFeaturedProductMapper;
        this.GMFeaturedProductSubmitMapper = GMFeaturedProductSubmitMapper;
    }

    @Override
    public Observable<GMFeaturedProductSubmitDomainModel> postFeatureProductData(RequestParams requestParams) {
        GMFeaturedProductSubmitModel GMFeaturedProductSubmitModel
                = (GMFeaturedProductSubmitModel) requestParams.getObject(GMFeaturedProductSubmitUseCase.FEATURED_PRODUCT_MODEL_PARAM);

        GMFeaturedProductSubmitModel.setShopId(shopInfoRepository.getShopId());


        return GMFeaturedProductDataSource.productPOSTModelObservable(GMFeaturedProductSubmitModel).map(GMFeaturedProductSubmitMapper);
    }

    @Override
    public Observable<GMFeaturedProductDomainModel> getFeatureProductData(RequestParams requestParams) {
        return GMFeaturedProductDataSource.productGETModelObservable(shopInfoRepository.getShopId()).map(GMFeaturedProductMapper);
    }
}
