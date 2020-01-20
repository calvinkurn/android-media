package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.usecase;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.repository.IMultipleAddressRepository;
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
    private final IMultipleAddressRepository multipleAddressRepository;
    private final ICartMapper cartMapper;
    private final Context context;

    @Inject
    public GetCartMultipleAddressListUseCase(@ApplicationContext Context context, IMultipleAddressRepository multipleAddressRepository, ICartMapper cartMapper) {
        this.context = context;
        this.multipleAddressRepository = multipleAddressRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<CartListData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING);
        return multipleAddressRepository.getCartMultipleAddressList(param)
                .map(new Func1<CartMultipleAddressDataListResponse, CartListData>() {
                    @Override
                    public CartListData call(CartMultipleAddressDataListResponse cartMultipleAddressDataListResponse) {
                        return cartMapper.convertToCartItemDataList(context, cartMultipleAddressDataListResponse);
                    }
                });
    }

}
