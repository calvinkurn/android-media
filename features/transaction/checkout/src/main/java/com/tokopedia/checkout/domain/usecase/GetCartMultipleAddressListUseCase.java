package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.transactiondata.entity.response.cartlist.CartMultipleAddressDataListResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author Irfan Khoirul on 31/08/18.
 */
public class GetCartMultipleAddressListUseCase extends UseCase<CartListData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";
    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final Context context;

    @Inject
    public GetCartMultipleAddressListUseCase(Context context, ICartRepository cartRepository, ICartMapper cartMapper) {
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
                .map(new Func1<CartMultipleAddressDataListResponse, CartListData>() {
                    @Override
                    public CartListData call(CartMultipleAddressDataListResponse cartMultipleAddressDataListResponse) {
                        return cartMapper.convertToCartItemDataList(context, cartMultipleAddressDataListResponse);
                    }
                });
    }

}
