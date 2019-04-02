package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.UpdateCartData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class UpdateAndReloadCartUseCase extends UseCase<UpdateAndRefreshCartListData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_GET_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_GET_CART";
    public static final String PARAM_CARTS = "carts";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final Context context;

    @Inject
    public UpdateAndReloadCartUseCase(Context context,
                                      ICartRepository cartRepository,
                                      ICartMapper cartMapper) {
        this.context = context;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<UpdateAndRefreshCartListData> createObservable(RequestParams requestParams) {
        final TKPDMapParam<String, String> paramUpdateCart = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        final TKPDMapParam<String, String> paramGetCart = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        return Observable.just(new UpdateAndRefreshCartListData())
                .flatMap(new Func1<UpdateAndRefreshCartListData, Observable<UpdateAndRefreshCartListData>>() {
                    @Override
                    public Observable<UpdateAndRefreshCartListData> call(final UpdateAndRefreshCartListData updateAndRefreshCartListData) {
                        return cartRepository.updateCartData(paramUpdateCart)
                                .map(new Func1<UpdateCartDataResponse, UpdateAndRefreshCartListData>() {
                                    @Override
                                    public UpdateAndRefreshCartListData call(UpdateCartDataResponse updateCartDataResponse) {
                                        UpdateCartData updateCartData = cartMapper.convertToUpdateCartData(updateCartDataResponse);
                                        updateAndRefreshCartListData.setUpdateCartData(updateCartData);
                                        return updateAndRefreshCartListData;
                                    }
                                });
                    }
                })
                .flatMap(new Func1<UpdateAndRefreshCartListData, Observable<UpdateAndRefreshCartListData>>() {
                    @Override
                    public Observable<UpdateAndRefreshCartListData> call(final UpdateAndRefreshCartListData updateAndRefreshCartListData) {
                        if (paramGetCart.containsKey(PARAM_CARTS)) {
                            paramGetCart.remove(PARAM_CARTS);
                        }
                        return cartRepository.getShopGroupList(paramGetCart)
                                .map(new Func1<CartDataListResponse, UpdateAndRefreshCartListData>() {
                                    @Override
                                    public UpdateAndRefreshCartListData call(CartDataListResponse cartDataListResponse) {
                                        CartListData cartListData = cartMapper.convertToCartItemDataList(
                                                context, cartDataListResponse
                                        );
                                        updateAndRefreshCartListData.setCartListData(cartListData);
                                        return updateAndRefreshCartListData;
                                    }
                                });
                    }
                });

    }
}
