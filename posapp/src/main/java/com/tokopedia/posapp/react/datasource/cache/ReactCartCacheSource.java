package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tokopedia.posapp.data.factory.CartFactory;
import com.tokopedia.posapp.data.pojo.CartResponse;
import com.tokopedia.posapp.data.source.local.CartLocalSource;
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
        return cartFactory.local().getCartProduct(productId).map(getCartMapper());
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
                CacheResult<CartDomain> result = new CacheResult<>();
                result.setData(cartDomain);
                return gson.toJson(result);
            }
        };
    }

    private Func1<List<CartDomain>, String> getCartListMapper() {
        return new Func1<List<CartDomain>, String>() {
            @Override
            public String call(List<CartDomain> cartDomains) {
                CacheResult<ListResult<CartDomain>> result = new CacheResult<>();
                ListResult<CartDomain> data = new ListResult<>();
                data.setList(cartDomains);
                result.setData(data);

                return gson.toJson(result);
            }
        };
    }

    private Func1<? super Boolean, ? extends String> getMockData() {
        return new Func1<Boolean, String>() {
            @Override
            public String call(Boolean aBoolean) {
                CacheResult<ListResult<CartResponse>> result = new CacheResult<>();

                ListResult<CartResponse> list = new ListResult<>();
                List<CartResponse> cartResponses = new ArrayList<>();
                CartResponse cartResponse = new CartResponse();
                cartResponse.setProductId("160551106");
                cartResponse.setPrice(20000);
                cartResponse.setProductName("Lovana Bodylotion Milky Cupcake 250ml");
                cartResponse.setQuantity(1);
                cartResponse.setImageUrl("https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/3/17/17437275/17437275_1878b3d2-ce9c-4ab3-b345-64da204ec935.jpg");
                cartResponses.add(cartResponse);

                cartResponse = new CartResponse();
                cartResponse.setProductId("160533448");
                cartResponse.setPrice(10000);
                cartResponse.setProductName("Happy Urang Aring 55ml");
                cartResponse.setQuantity(2);
                cartResponse.setImageUrl("https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/4/28/160533448/160533448_8ee45562-709b-4da1-8505-355282ac5459_1000_1000.jpg");
                cartResponses.add(cartResponse);

                cartResponse = new CartResponse();
                cartResponse.setProductId("193938857");
                cartResponse.setPrice(30000);
                cartResponse.setProductName("Oh Man! Baby Pomade Nutri Green 45gr");
                cartResponse.setQuantity(3);
                cartResponse.setImageUrl("https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/8/10/193938857/193938857_022ba5db-40b1-4ca2-b460-aed833272f5b_1000_1000.jpg");
                cartResponses.add(cartResponse);

                list.setList(cartResponses);
                result.setData(list);

                return gson.toJson(result);
            }
        };
    }
}
