package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.posapp.cart.data.pojo.CartResponse;
import com.tokopedia.posapp.cart.data.source.CartLocalSource;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
import com.tokopedia.posapp.product.common.data.pojo.ProductDetail;
import com.tokopedia.posapp.react.datasource.ReactDataSource;
import com.tokopedia.posapp.react.datasource.model.CacheResult;
import com.tokopedia.posapp.react.datasource.model.ListResult;
import com.tokopedia.posapp.react.datasource.model.StatusResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/28/17.
 */

public class ReactCartCacheSource extends ReactDataSource {
    private CartLocalSource cartLocalSource;

    @Inject
    public ReactCartCacheSource(CartLocalSource cartLocalSource,
                                Gson gson) {
        super(gson);
        this.cartLocalSource = cartLocalSource;
    }

    @Override
    public Observable<String> getData(String productId) {
        return cartLocalSource.getCartProduct(Integer.parseInt(productId)).map(getCartMapper());
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return cartLocalSource.getCartProducts(offset, limit).map(getCartListMapper());
    }

    @Override
    public Observable<String> getDataAll() {
        return cartLocalSource.getAllCartProducts().map(getCartListMapper());
    }

    @Override
    public Observable<String> deleteAll() {
        return cartLocalSource.deleteCart().map(getDbOperationMapper());
    }

    @Override
    public Observable<String> deleteItem(String id) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(Integer.parseInt(id));
        return cartLocalSource.deleteCartProduct(cartDomain).map(getDbOperationMapper());
    }

    @Override
    public Observable<String> update(String data) {
        List<CartResponse> cartResponse = gson.fromJson(data, new TypeToken<List<CartResponse>>(){}.getType());
        return Observable.just(cartResponse)
                .flatMapIterable(
                    new Func1<List<CartResponse>, Iterable<CartResponse>>() {
                        @Override
                        public Iterable<CartResponse> call(List<CartResponse> cartResponses) {
                            return cartResponses;
                        }
                    }
                ).flatMap(new Func1<CartResponse, Observable<ATCStatusDomain>>() {
                    @Override
                    public Observable<ATCStatusDomain> call(CartResponse cartResponse) {
                        return cartLocalSource.updateCartProduct(mapToDomain(cartResponse));
                    }
                }).toList().map(getUpdateMapper());
    }

    @Override
    public Observable<String> insert(String data) {
        return Observable.error(new RuntimeException("Method not implemented yet"));
    }

    private CartDomain mapToDomain(CartResponse cartResponse) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setId(cartResponse.getId());
        cartDomain.setProductId(cartResponse.getProductId());
        cartDomain.setQuantity(cartResponse.getQuantity());
        cartDomain.setProductName(cartResponse.getProduct().getProductName());
        cartDomain.setProductPrice(cartResponse.getProduct().getProductPrice());
        cartDomain.setProductImage(cartResponse.getProduct().getProductImage());
        cartDomain.setProductImage300(cartResponse.getProduct().getProductImage300());
        cartDomain.setProductImageFull(cartResponse.getProduct().getProductImageFull());
        cartDomain.setProductPriceUnformatted(cartResponse.getProduct().getProductPriceUnformatted());
        return cartDomain;
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

    private Func1<List<CartDomain>, String> getCartListMapper() {
        return new Func1<List<CartDomain>, String>() {
            @Override
            public String call(List<CartDomain> cartDomains) {
                CacheResult<ListResult<CartResponse>> result = new CacheResult<>();

                ListResult<CartResponse> list = new ListResult<>();
                List<CartResponse> cartResponses = new ArrayList<>();
                for (CartDomain cartDomain : cartDomains) {
                    cartResponses.add(getCartResponse(cartDomain));
                }

                list.setList(cartResponses);
                result.setData(list);

                return gson.toJson(result);
            }
        };
    }

    private Func1<List<ATCStatusDomain>, String> getUpdateMapper() {
        return new Func1<List<ATCStatusDomain>, String>() {
            @Override
            public String call(List<ATCStatusDomain> atcStatusDomain) {
                CacheResult<StatusResult> response = new CacheResult<>();
                StatusResult statusResult = new StatusResult();
//                if (atcStatusDomain.getStatus() == ATCStatusDomain.RESULT_ADD_TO_CART_SUCCESS) {
                    statusResult.setStatus(true);
                    statusResult.setMessage("");
//                } else {
//                    statusResult.setStatus(false);
//                    statusResult.setMessage(atcStatusDomain.getMessage());
//                }
                response.setData(statusResult);
                return gson.toJson(response);
            }
        };
    }

    private Func1<ATCStatusDomain, String> getDbOperationMapper() {
        return new Func1<ATCStatusDomain, String>() {
            @Override
            public String call(ATCStatusDomain atcStatusDomain) {
                CacheResult<StatusResult> response = new CacheResult<>();
                StatusResult statusResult = new StatusResult();
                if (atcStatusDomain.getStatus() == ATCStatusDomain.RESULT_ADD_TO_CART_SUCCESS) {
                    statusResult.setStatus(true);
                    statusResult.setMessage(atcStatusDomain.getMessage());
                } else {
                    statusResult.setStatus(false);
                    statusResult.setMessage(atcStatusDomain.getMessage());
                }
                response.setData(statusResult);
                return gson.toJson(response);
            }
        };
    }

    private CartResponse getCartResponse(CartDomain cartDomain) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cartDomain.getId());
        cartResponse.setProductId(cartDomain.getProductId());
        cartResponse.setProductName(cartDomain.getProductName());
        cartResponse.setQuantity(cartDomain.getQuantity());
        cartResponse.setUnitPrice(cartDomain.getProductPriceUnformatted());
        cartResponse.setTotalPrice(cartDomain.getProductPriceUnformatted()*cartDomain.getQuantity());

        ProductDetail product = new ProductDetail();
        product.setProductId(cartDomain.getProductId());
        product.setProductName(cartDomain.getProductName());
        product.setProductPrice(cartDomain.getProductPrice());
        product.setProductUrl(cartDomain.getProductUrl());
        product.setProductImage(cartDomain.getProductImage());
        product.setProductImage300(cartDomain.getProductImage300());
        product.setProductImageFull(cartDomain.getProductImageFull());
        product.setProductPriceUnformatted(cartDomain.getProductPriceUnformatted());
        cartResponse.setProduct(product);

        return cartResponse;
    }
}
