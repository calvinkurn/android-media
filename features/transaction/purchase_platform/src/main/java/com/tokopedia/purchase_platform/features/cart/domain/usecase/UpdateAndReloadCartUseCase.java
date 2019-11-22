package com.tokopedia.purchase_platform.features.cart.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class UpdateAndReloadCartUseCase extends UseCase<UpdateAndRefreshCartListData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART";
    public static final String PARAM_CARTS = "carts";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private GetCartListSimplifiedUseCase getCartListSimplifiedUseCase;

    @Inject
    public UpdateAndReloadCartUseCase(ICartRepository cartRepository,
                                      ICartMapper cartMapper,
                                      GetCartListSimplifiedUseCase getCartListSimplifiedUseCase) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.getCartListSimplifiedUseCase = getCartListSimplifiedUseCase;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<UpdateAndRefreshCartListData> createObservable(RequestParams requestParams) {
        final TKPDMapParam<String, String> paramUpdateCart = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART);

        return Observable.just(new UpdateAndRefreshCartListData())
                .flatMap(updateAndRefreshCartListData -> cartRepository.updateCartData(paramUpdateCart)
                        .map(updateCartDataResponse -> {
                            UpdateCartData updateCartData = cartMapper.convertToUpdateCartData(updateCartDataResponse);
                            updateAndRefreshCartListData.setUpdateCartData(updateCartData);
                            return updateAndRefreshCartListData;
                        }))
                .flatMap(updateAndRefreshCartListData ->
                        getCartListSimplifiedUseCase.createObservable(RequestParams.EMPTY)
                                .map(cartListData -> {
                                    updateAndRefreshCartListData.setCartListData(cartListData);
                                    return updateAndRefreshCartListData;
                                })
                );

    }
}
