package com.tokopedia.purchase_platform.features.cart.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.features.cart.data.model.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class UpdateCartUseCase extends UseCase<UpdateCartData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART";
    public static final String PARAM_CARTS = "carts";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;

    @Inject
    public UpdateCartUseCase(ICartRepository cartRepository,
                             ICartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<UpdateCartData> createObservable(RequestParams requestParams) {
        final TKPDMapParam<String, String> paramUpdate = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        return cartRepository.updateCartData(paramUpdate).map(new Func1<UpdateCartDataResponse, UpdateCartData>() {
            @Override
            public UpdateCartData call(UpdateCartDataResponse updateCartDataResponse) {
                return cartMapper.convertToUpdateCartData(updateCartDataResponse);
            }
        });
    }
}
