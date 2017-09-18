package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.posapp.data.pojo.Paging;
import com.tokopedia.posapp.data.pojo.ShopProductResponse;
import com.tokopedia.posapp.database.manager.ProductDbManager;
import com.tokopedia.posapp.database.model.ProductDb;
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
    private ProductDbManager productDbManager;

    public ReactProductCacheSource() {
        gson = new Gson();
        productDbManager = new ProductDbManager();
    }

    @Override
    public Observable<String> getData(String id) {
        return null;
    }

    @Override
    public Observable<String> getListData(int offset, int limit) {
        return null;
    }

    @Override
    public Observable<String> getAllData() {
        List<ProductDb> productDbs = productDbManager.getAllData();
        ShopProductResponse shopProductResponse = new ShopProductResponse();
        List<com.tokopedia.core.shopinfo.models.productmodel.List> productList = new ArrayList<>();
        for(ProductDb productDb : productDbs) {
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
        shopProductResponse.setTotalData(productDbs.size());
        shopProductResponse.setPaging(new Paging());

        final CacheResult<ShopProductResponse> response = new CacheResult<>();
        response.setData(shopProductResponse);

        return Observable.just(response)
                .map(new Func1<CacheResult, String>() {
                    @Override
                    public String call(CacheResult response) {
                        return gson.toJson(response);
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
}
