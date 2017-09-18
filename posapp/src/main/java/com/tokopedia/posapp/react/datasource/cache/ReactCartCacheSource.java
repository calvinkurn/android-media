package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.posapp.data.factory.CartFactory;
import com.tokopedia.posapp.data.pojo.CartResponse;
import com.tokopedia.posapp.domain.model.cart.CartDomain;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.react.datasource.model.ListResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactCartCacheSource implements ReactCacheSource {
    private Gson gson;
    private CartFactory cartFactory;

    public ReactCartCacheSource(CartFactory cartFactory, Gson gson) {
        this.gson = gson;
        this.cartFactory = cartFactory;
    }

    @Override
    public Observable<String> getData(String productId) {
        return cartFactory.local().getCartProduct(Integer.parseInt(productId)).map(getCartMapper());
    }

    @Override
    public Observable<String> getListData(int offset, int limit) {
        return cartFactory.local().getCartProducts(offset, limit).map(getCartListMapper());
    }

    @Override
    public Observable<String> getAllData() {
        return cartFactory.local().getAllCartProducts().map(getCartListMapper());
    }

    private Func1<CartDomain, String> getCartMapper() {
        return new Func1<CartDomain, String>() {
            @Override
            public String call(CartDomain cartDomain) {
                CacheResult<CartResponse> result = new CacheResult<>();
                result.setData(getCartResponse(cartDomain));
                return gson.toJson(result);
            }
        };
    }

    private Func1< List<CartDomain>,String> getCartListMapper() {
        return new Func1<List<CartDomain>, String>() {
            @Override
            public String call(List<CartDomain> cartDomains) {
                CacheResult<ListResult<CartResponse>> result = new CacheResult<>();

                ListResult<CartResponse> list = new ListResult<>();
                List<CartResponse> cartResponses = new ArrayList<>();
                for(CartDomain cartDomain : cartDomains) {
                    cartResponses.add(getCartResponse(cartDomain));
                }

                list.setList(cartResponses);
                result.setData(list);

                return gson.toJson(result);
            }
        };
    }

    private CartResponse getCartResponse(CartDomain cartDomain) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setQuantity(cartDomain.getQuantity());
        cartResponse.setProductId(cartDomain.getProductId());

        com.tokopedia.core.shopinfo.models.productmodel.List product = new com.tokopedia.core.shopinfo.models.productmodel.List();
        product.productId = cartDomain.getProductId();
        product.productName = cartDomain.getProduct().getProductName();
        product.productPrice = cartDomain.getProduct().getProductPrice();
        product.productUrl = cartDomain.getProduct().getProductUrl();
        product.productImage = cartDomain.getProduct().getProductImage();
        product.productImage300 = cartDomain.getProduct().getProductImage300();
        product.productImageFull = cartDomain.getProduct().getProductImageFull();
        cartResponse.setProduct(product);

        return cartResponse;
    }
}
