package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 25/04/18.
 */
public class GetCartListUseCase extends UseCase<CartListData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";
    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final Context context;

    @Inject
    public GetCartListUseCase(Context context, ICartRepository cartRepository, ICartMapper cartMapper) {
        this.context = context;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<CartListData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING);
        return cartRepository.getCartList(param)
                .map(new Func1<CartDataListResponse, CartListData>() {
                    @Override
                    public CartListData call(CartDataListResponse cartDataListResponse) {
                        return cartMapper.convertToCartItemDataList(context, cartDataListResponse);
                    }
                });
    }

}
