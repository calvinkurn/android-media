package com.tokopedia.attachproduct.data.repository;

import com.tokopedia.attachproduct.data.model.mapper.TkpdResponseToAttachProductDomainModelMapper;
import com.tokopedia.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.attachproduct.domain.model.AttachProductDomainModel;

import java.util.Map;

import rx.Observable;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductRepositoryImpl implements AttachProductRepository {
    private final GetShopProductService shopService;
    private final TkpdResponseToAttachProductDomainModelMapper mapper;
    public AttachProductRepositoryImpl(GetShopProductService shopService, TkpdResponseToAttachProductDomainModelMapper mapper) {
        this.shopService = shopService;
        this.mapper = mapper;
    }

    @Override
    public Observable<AttachProductDomainModel> loadProductFromShop(Map<String,String> params) {
        return shopService.getApi().getShopProduct(params).map(mapper);
    }

}
