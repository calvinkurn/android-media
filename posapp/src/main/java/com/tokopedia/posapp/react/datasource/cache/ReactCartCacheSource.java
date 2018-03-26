package com.tokopedia.posapp.react.datasource.cache;

import com.google.gson.Gson;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.posapp.cart.data.factory.CartFactory;
import com.tokopedia.posapp.cart.data.pojo.CartResponse;
import com.tokopedia.posapp.cart.domain.model.ATCStatusDomain;
import com.tokopedia.posapp.cart.domain.model.CartDomain;
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
    private CartFactory cartFactory;

    @Inject
    public ReactCartCacheSource(CartFactory cartFactory,
                                Gson gson) {
        super(gson);
        this.cartFactory = cartFactory;
    }

    @Override
    public Observable<String> getData(String productId) {
        return cartFactory.local().getCartProduct(Integer.parseInt(productId)).map(getCartMapper());
    }

    @Override
    public Observable<String> getDataList(int offset, int limit) {
        return cartFactory.local().getCartProducts(offset, limit).map(getCartListMapper());
    }

    @Override
    public Observable<String> getDataAll() {
        return cartFactory.local().getAllCartProducts().map(getCartListMapper());
    }

    @Override
    public Observable<String> deleteAll() {
        return cartFactory.local().deleteCart().map(getDbOperationMapper());
    }

    @Override
    public Observable<String> deleteItem(String id) {
        CartDomain cartDomain = new CartDomain();
        cartDomain.setProductId(Integer.parseInt(id));
        return cartFactory.local().deleteCartProduct(cartDomain).map(getDbOperationMapper());
    }

    @Override
    public Observable<String> update(String data) {
        CartResponse cartResponse = gson.fromJson(data, CartResponse.class);
        return cartFactory.local().updateCartProduct(mapToDomain(cartResponse)).map(getDbOperationMapper());
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
        product.productPriceUnformatted = CurrencyFormatHelper.convertRupiahToInt(cartDomain.getProduct().getProductPrice());
        cartResponse.setProduct(product);

        return cartResponse;
    }
}
