package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.posapp.product.ProductFactory;
import com.tokopedia.posapp.base.data.pojo.Paging;
import com.tokopedia.posapp.shop.data.ShopProductResponse;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.react.datasource.model.CacheResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactProductCacheSource implements ReactCacheSource {
    private Gson gson;
    private ProductFactory productFactory;

    public ReactProductCacheSource(ProductFactory productFactory, Gson gson) {
        this.gson = gson;
        this.productFactory = productFactory;
    }

    @Override
    public Observable<String> getData(String id) {
        try {
            int productId = Integer.parseInt(id);
            return productFactory.local().getProduct(productId)
                    .map(getProductMapper())
                    .map(mapToJson());
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return productFactory.local().getListProduct(offset, limit)
                .map(getListMapper())
                .map(mapToJson());
    }

    @Override
    public Observable<String> getDataAll() {
        return productFactory.local().getAllProduct()
                .map(getListMapper())
                .map(mapToJson());
    }

    @Override
    public Observable<String> deleteAll() {
        return null;
    }

    @Override
    public Observable<String> deleteItem(String id) {
        return null;
    }

    @Override
    public Observable<String> update(String data) {
        return null;
    }

    @Override
    public Observable<String> insert(String data) {
        return Observable.error(new RuntimeException("Method not implemented yet"));
    }

    public Observable<String> search(String keyword, String etalaseId) {
        return productFactory.local().searchProduct(keyword, etalaseId)
                .map(getListMapper())
                .map(mapToJson());
    }

    private Func1<List<ProductDomain>, CacheResult> getListMapper() {
        return new Func1<List<ProductDomain>, CacheResult>() {
            @Override
            public CacheResult call(List<ProductDomain> productDomains) {
                ShopProductResponse shopProductResponse = new ShopProductResponse();
                List<com.tokopedia.core.shopinfo.models.productmodel.List> productList = new ArrayList<>();
                for (ProductDomain productDomain : productDomains) {
                    productList.add(domainToResponse(productDomain));
                }
                shopProductResponse.setList(productList);
                shopProductResponse.setTotalData(productDomains.size());
                shopProductResponse.setPaging(new Paging());

                CacheResult<ShopProductResponse> cacheResult = new CacheResult<>();
                cacheResult.setData(shopProductResponse);
                return cacheResult;
            }
        };
    }

    private Func1<ProductDomain, CacheResult> getProductMapper() {
        return new Func1<ProductDomain, CacheResult>() {
            @Override
            public CacheResult call(ProductDomain productDomain) {
                CacheResult<com.tokopedia.core.shopinfo.models.productmodel.List> cacheResult =
                        new CacheResult<>();
                cacheResult.setData(domainToResponse(productDomain));

                return cacheResult;
            }
        };
    }

    private com.tokopedia.core.shopinfo.models.productmodel.List domainToResponse(ProductDomain productDomain) {
        com.tokopedia.core.shopinfo.models.productmodel.List item = new com.tokopedia.core.shopinfo.models.productmodel.List();
        item.productName = productDomain.getProductName();
        item.productPrice = productDomain.getProductPrice();
        item.productId = productDomain.getProductId();
        item.productImage = productDomain.getProductImage();
        item.productImage300 = productDomain.getProductImage300();
        item.productImageFull = productDomain.getProductImageFull();
        return item;
    }

    private Func1<CacheResult, String> mapToJson() {
        return new Func1<CacheResult, String>() {
            @Override
            public String call(CacheResult cacheResult) {
                return gson.toJson(cacheResult);
            }
        };
    }
}
