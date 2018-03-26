package com.tokopedia.posapp.react.datasource.cloud;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.shopinfo.models.productmodel.List;
import com.tokopedia.core.shopinfo.models.productmodel.Product;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.base.data.pojo.Paging;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepository;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepositoryImpl;
import com.tokopedia.posapp.etalase.EtalaseResponse;
import com.tokopedia.posapp.product.common.data.repository.ProductCloudRepository;
import com.tokopedia.posapp.product.common.data.repository.ProductRepository;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.react.datasource.model.ProductSearchRequest;
import com.tokopedia.posapp.shop.data.ShopProductResponse;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 3/22/18.
 */

public class ReactProductCloudSource extends ReactDataSource {
    private static final String SHOP_ID = "shop_id";
    private static final String START_OFFSET = "startoffset";
    private static final String ROW_OFFSET = "rowoffset";
    private static final String DATA_PER_ROW = "data_per_row";
    private static final String ETALASE = "etalase";
    private static final String KEYWORD = "keyword";

    private ProductRepository productRepository;
    private EtalaseRepository etalaseRepository;
    private SessionHandler sessionHandler;

    @Inject
    ReactProductCloudSource(ProductCloudRepository productRepository,
                            EtalaseRepositoryImpl etalaseRepository,
                            Gson gson,
                            SessionHandler sessionHandler) {
        super(gson);
        this.productRepository = productRepository;
        this.etalaseRepository = etalaseRepository;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<String> getData(String id) {
        return null;
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, sessionHandler.getShopID());
        requestParams.putString(START_OFFSET, Integer.toString(offset));
        requestParams.putString(ROW_OFFSET, Integer.toString(limit));
        requestParams.putInt(DATA_PER_ROW, limit);
        return productRepository.getProductList(requestParams).map(getListMapper()).map(mapToJson());
    }

    @Override
    public Observable<String> getDataAll() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, sessionHandler.getShopID());
        requestParams.putString(START_OFFSET, Integer.toString(0));
        requestParams.putString(ROW_OFFSET, Integer.toString(10));
        requestParams.putInt(DATA_PER_ROW, 10);
        return productRepository.getProductList(requestParams).map(getListMapper()).map(mapToJson());
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
        return null;
    }

    private Func1<ProductListDomain, CacheResult> getListMapper() {
        return new Func1<ProductListDomain, CacheResult>() {
            @Override
            public CacheResult call(ProductListDomain productDomains) {
                ShopProductResponse shopProductResponse = new ShopProductResponse();
                java.util.List<List> productList = new ArrayList<>();
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

    public Observable<String> search(final ProductSearchRequest request) {
        return etalaseRepository
                .getEtalaseCache()
                .flatMap(new Func1<java.util.List<EtalaseDomain>, Observable<String>>() {
                    @Override
                    public Observable<String> call(java.util.List<EtalaseDomain> etalaseDomains) {
                        RequestParams requestParams = RequestParams.EMPTY;
                        requestParams.putString(KEYWORD, request.getKeyword());
                        requestParams.putString(SHOP_ID, sessionHandler.getShopID());
                        if(TextUtils.isEmpty(request.getEtalaseId())) {
                            request.setEtalaseId(etalaseDomains.get(0).getEtalaseId());
                        }
                        requestParams.putString(ETALASE, request.getEtalaseId());
                        if(request.getOffset() == null || request.getLimit() == null) {
                            request.setOffset(0);
                            request.setLimit(10);
                        }
                        requestParams.putString(START_OFFSET, request.getOffset().toString());
                        requestParams.putString(ROW_OFFSET, request.getLimit().toString());
                        return productRepository.getProductList(requestParams).map(getListMapper()).map(mapToJson());
                    }
                });
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
