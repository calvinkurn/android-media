package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.posapp.base.data.pojo.Paging;
import com.tokopedia.posapp.product.common.data.repository.ProductLocalRepository;
import com.tokopedia.posapp.product.common.data.repository.ProductRepository;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.shop.data.ShopProductResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactProductCacheSource extends ReactDataSource {
    private ProductRepository productRepository;

    @Inject
    public ReactProductCacheSource(ProductLocalRepository productRepository,
                                   Gson gson) {
        super(gson);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<String> getData(String id) {
        try {
            RequestParams requestParams = RequestParams.EMPTY;
            requestParams.putInt(ProductRepository.PRODUCT_ID, Integer.parseInt(id));
            return productRepository.getProductDomain(requestParams)
                    .map(getProductMapper())
                    .map(mapToJson());
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        RequestParams requestParams = RequestParams.EMPTY;
        requestParams.putInt(ProductRepository.OFFSET, offset);
        requestParams.putInt(ProductRepository.LIMIT, limit);
        return productRepository.getProductList(requestParams)
                .map(getListMapper())
                .map(mapToJson());
    }

    @Override
    public Observable<String> getDataAll() {
        return productRepository.getProductList(RequestParams.EMPTY)
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
        RequestParams requestParams = RequestParams.EMPTY;
        requestParams.putString(ProductRepository.KEYWORD, keyword);
        requestParams.putString(ProductRepository.ETALASE_ID, etalaseId);
        return productRepository.getProductList(requestParams).map(getListMapper()).map(mapToJson());
    }

    private Func1<ProductListDomain, CacheResult> getListMapper() {
        return new Func1<ProductListDomain, CacheResult>() {
            @Override
            public CacheResult call(ProductListDomain productDomains) {
                ShopProductResponse shopProductResponse = new ShopProductResponse();
                List<com.tokopedia.core.shopinfo.models.productmodel.List> productList = new ArrayList<>();
                for (ProductDomain productDomain : productDomains.getProductDomains()) {
                    productList.add(domainToResponse(productDomain));
                }
                shopProductResponse.setList(productList);
                shopProductResponse.setTotalData(productDomains.getProductDomains().size());
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
}
