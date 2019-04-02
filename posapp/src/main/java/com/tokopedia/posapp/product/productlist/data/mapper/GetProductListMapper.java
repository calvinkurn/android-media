package com.tokopedia.posapp.product.productlist.data.mapper;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductDetail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 10/17/17.
 */

public class GetProductListMapper implements Func1<Response<PosSimpleResponse<List<ProductDetail>>>, Observable<? extends List<ProductDomain>>> {

    private static final String NO_PRODUCT = "no product";

    @Inject
    public GetProductListMapper() {
    }

    @Override
    public Observable<List<ProductDomain>> call(Response<PosSimpleResponse<List<ProductDetail>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            if(response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase(NO_PRODUCT)) {
                List<ProductDomain> empty = new ArrayList<>();
                return Observable.just(empty);
            }

            if(response.body().getData() != null && response.body().getData().getData() != null) {
                return Observable
                        .just(response.body().getData().getData())
                        .map(mapToDomain()).flatMapIterable(iterateList()).filter(filterData()).toList();
            }
        }

        return Observable.error(new RuntimeException("No Product"));
    }

    private Func1<List<ProductDetail>, List<ProductDomain>> mapToDomain() {
        return new Func1<List<ProductDetail>, List<ProductDomain>>() {
            @Override
            public List<ProductDomain> call(List<ProductDetail> productDetails) {
                List<ProductDomain> domains = new ArrayList<>();

                for (ProductDetail item : productDetails) {
                    ProductDomain productDomain = new ProductDomain();
                    productDomain.setId(item.getId());
                    productDomain.setName(item.getName());
                    productDomain.setDescription(item.getProductDescription());
                    productDomain.setPriceUnformatted(item.getLocalPrice().getPrice());
                    productDomain.setPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(item.getLocalPrice().getPrice(), true));
                    productDomain.setOriginalPriceUnformatted(item.getPrice());
                    productDomain.setUrl(item.getUrl());
                    productDomain.setPictures(item.getPictures());
                    if(item.getPictures() != null && item.getPictures().size() > 0) {
                        productDomain.setImage(item.getPictures().get(0).getUrlOriginal());
                        productDomain.setImage300(item.getPictures().get(0).getUrlThumbnail());
                        productDomain.setImageFull(item.getPictures().get(0).getUrlOriginal());
                    }
                    productDomain.setStatus(item.getLocalPrice().getStatus());
                    domains.add(productDomain);
                }

                return domains;
            }
        };
    }

    private Func1<List<ProductDomain>, Iterable<ProductDomain>> iterateList() {
        return new Func1<List<ProductDomain>, Iterable<ProductDomain>>() {
            @Override
            public Iterable<ProductDomain> call(List<ProductDomain> productDomains) {
                return productDomains;
            }
        };
    }

    private Func1<ProductDomain, Boolean> filterData() {
        return new Func1<ProductDomain, Boolean>() {
            @Override
            public Boolean call(ProductDomain productDomain) {
                return productDomain.getStatus() == ProductConstant.Status.LOCAL_PRICE_SHOW;
            }
        };
    }
}
