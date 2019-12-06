package com.tokopedia.purchase_platform.features.cart.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers;
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class UpdateCartUseCase {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART";
    public static final String PARAM_CARTS = "carts";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final ExecutorSchedulers schedulers;

    @Inject
    public UpdateCartUseCase(ICartRepository cartRepository,
                             ICartMapper cartMapper, ExecutorSchedulers schedulers) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.schedulers = schedulers;
    }

    @SuppressWarnings("unchecked")
    public Observable<UpdateCartData> createObservable(RequestParams requestParams) {
        final TKPDMapParam<String, String> paramUpdate = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        return cartRepository.updateCartData(paramUpdate)
                .map(cartMapper::convertToUpdateCartData)
                .subscribeOn(schedulers.getIo())
                .observeOn(schedulers.getMain());
    }
}
