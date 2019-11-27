package com.tokopedia.purchase_platform.features.cart.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.features.cart.domain.model.ResetAndRefreshCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.features.cart.data.model.response.CartDataListResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class ResetCartGetCartListUseCase extends UseCase<ResetAndRefreshCartListData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART";
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_GET_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_GET_CART";
    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final Context context;

    @Inject
    public ResetCartGetCartListUseCase(@ApplicationContext Context context, ICartRepository cartRepository, ICartMapper cartMapper) {
        this.context = context;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<ResetAndRefreshCartListData> createObservable(RequestParams requestParams) {

        final TKPDMapParam<String, String> paramReset = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART);

        final TKPDMapParam<String, String> paramGetCart = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_GET_CART);

        return Observable.just(new ResetAndRefreshCartListData())
                .flatMap(new Func1<ResetAndRefreshCartListData, Observable<ResetAndRefreshCartListData>>() {
                    @Override
                    public Observable<ResetAndRefreshCartListData> call(final ResetAndRefreshCartListData resetAndRefreshCartListData) {
                        return cartRepository.getShopGroupList(paramGetCart)
                                .map(new Func1<CartDataListResponse, ResetAndRefreshCartListData>() {
                                    @Override
                                    public ResetAndRefreshCartListData call(
                                            CartDataListResponse cartDataListResponse
                                    ) {
                                        CartListData cartListData = cartMapper.convertToCartItemDataList(
                                                context, cartDataListResponse
                                        );
                                        resetAndRefreshCartListData.setCartListData(cartListData);
                                        return resetAndRefreshCartListData;
                                    }
                                });
                    }
                });
    }
}
