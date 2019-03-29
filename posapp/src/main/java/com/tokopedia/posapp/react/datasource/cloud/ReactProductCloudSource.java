package com.tokopedia.posapp.react.datasource.cloud;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.base.data.pojo.Paging;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.common.data.pojo.ProductDetail;
import com.tokopedia.posapp.product.common.data.repository.ProductCloudRepository;
import com.tokopedia.posapp.product.common.data.repository.ProductRepository;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.react.datasource.model.ProductSearchRequest;
import com.tokopedia.posapp.product.common.data.pojo.ShopProductResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 3/22/18.
 */

public class ReactProductCloudSource extends ReactDataSource {

    private ProductRepository productRepository;
    private PosSessionHandler sessionHandler;
    private UserSession userSession;

    @Inject
    ReactProductCloudSource(ProductCloudRepository productRepository,
                            Gson gson,
                            PosSessionHandler sessionHandler,
                            UserSession userSession) {
        super(gson);
        this.productRepository = productRepository;
        this.sessionHandler = sessionHandler;
        this.userSession = userSession;
    }

    @Override
    public Observable<String> getData(String id) {
        return null;
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return Observable.error(new RuntimeException("Method not implemented"));
    }

    @Override
    public Observable<String> getDataAll() {
        return Observable.error(new RuntimeException("Method not implemented"));
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

    private Func1<List<ProductDomain>, CacheResult> getListMapper() {
        return new Func1<List<ProductDomain>, CacheResult>() {
            @Override
            public CacheResult call(List<ProductDomain> productDomains) {
                ShopProductResponse shopProductResponse = new ShopProductResponse();
                List<ProductDetail> productList = new ArrayList<>();
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

    public Observable<String> search(final ProductSearchRequest request) {
        if (request.getPage() == null) request.setPage(1);
        if (request.getLimit() == null) request.setLimit(18);

        RequestParams requestParams = RequestParams.create();

        if(!TextUtils.isEmpty(request.getKeyword())) {
            requestParams.putString(ProductConstant.Key.KEYWORD, request.getKeyword());
        }

        if (!TextUtils.isEmpty(request.getEtalaseId())) {
            requestParams.putString(ProductConstant.Key.ETALASE_ID, request.getEtalaseId());
        }

        requestParams.putString(ProductConstant.Key.PAGE, request.getPage().toString());
        requestParams.putString(ProductConstant.Key.PER_PAGE, request.getLimit().toString());
        requestParams.putString(ProductConstant.Key.OUTLET_ID, sessionHandler.getOutletId());
        requestParams.putString(ProductConstant.Key.SHOP_ID, userSession.getShopId());
        return productRepository.getProductList(requestParams).map(getListMapper()).map(mapToJson());
    }

    private ProductDetail domainToResponse(ProductDomain productDomain) {
        ProductDetail item = new ProductDetail();
        item.setProductName(productDomain.getName());
        item.setProductDescription(productDomain.getDescription());
        item.setProductPrice(productDomain.getPrice());
        item.setProductId(productDomain.getId());
        item.setProductImage(productDomain.getImage());
        item.setProductImage300(productDomain.getImage300());
        item.setProductImageFull(productDomain.getImageFull());
        item.setPictures(productDomain.getPictures());
        item.setProductPriceUnformatted(productDomain.getPriceUnformatted());
        item.setProductPriceOriginal(productDomain.getOriginalPriceUnformatted());
        item.setProductPriceOriginalFormat(CurrencyFormatUtil.convertPriceValueToIdrFormat(productDomain.getOriginalPriceUnformatted(), true));
        return item;
    }
}
