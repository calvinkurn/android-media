package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.posapp.data.factory.ProductFactory;
import com.tokopedia.posapp.data.pojo.Paging;
import com.tokopedia.posapp.data.pojo.ShopProductResponse;
import com.tokopedia.posapp.database.manager.ProductDbManager2;
import com.tokopedia.posapp.domain.model.product.ProductDomain;
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
    private ProductDbManager2 productDbManager;
    private ProductFactory productFactory;

    public ReactProductCacheSource() {
        gson = new Gson();
        productDbManager = new ProductDbManager2();
    }

    @Override
    public Observable<String> getData(String id) {
        return null;
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return null;
    }

    @Override
    public Observable<String> getDataAll() {
        return productDbManager.getAllData()
                .map(mapToResponse())
                .map(new Func1<CacheResult, String>() {
                    @Override
                    public String call(CacheResult cacheResult) {
                        return gson.toJson(cacheResult);
                    }
                });
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

    public Observable<String> searchProduct(String keyword, String etalaseId) {
        return productDbManager.search(keyword, etalaseId)
                .map(mapToResponse())
                .map(new Func1<CacheResult, String>() {
                    @Override
                    public String call(CacheResult cacheResult) {
                        return gson.toJson(cacheResult);
                    }
                });
    }

    private Func1<List<ProductDomain>, CacheResult> mapToResponse() {
        return new Func1<List<ProductDomain>, CacheResult>() {
            @Override
            public CacheResult call(List<ProductDomain> productDomains) {
                ShopProductResponse shopProductResponse = new ShopProductResponse();
                List<com.tokopedia.core.shopinfo.models.productmodel.List> productList = new ArrayList<>();
                for (ProductDomain productDb : productDomains) {
                    com.tokopedia.core.shopinfo.models.productmodel.List item = new com.tokopedia.core.shopinfo.models.productmodel.List();
                    item.productName = productDb.getProductName();
                    item.productPrice = productDb.getProductPrice();
                    item.productId = productDb.getProductId();
                    item.productImage = productDb.getProductImage();
                    item.productImage300 = productDb.getProductImage300();
                    item.productImageFull = productDb.getProductImageFull();
                    productList.add(item);
                }
                shopProductResponse.setList(productList);
                shopProductResponse.setTotalData(productDomains.size());
                shopProductResponse.setPaging(new Paging());

                CacheResult<ShopProductResponse> response = new CacheResult<>();
                response.setData(shopProductResponse);
                return response;
            }
        };
    }
}
