package com.tokopedia.purchase_platform.features.cart.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers;
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository;
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateAndRefreshCartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class UpdateAndReloadCartUseCase {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART";
    public static final String PARAM_CARTS = "carts";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final GetCartListSimplifiedUseCase getCartListSimplifiedUseCase;
    private final ExecutorSchedulers schedulers;

    @Inject
    public UpdateAndReloadCartUseCase(ICartRepository cartRepository,
                                      ICartMapper cartMapper,
                                      @Named("UpdateReloadUseCase")
                                              GetCartListSimplifiedUseCase getCartListSimplifiedUseCase,
                                      ExecutorSchedulers schedulers) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.getCartListSimplifiedUseCase = getCartListSimplifiedUseCase;
        this.schedulers = schedulers;
    }

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
                        getCartListSimplifiedUseCase.createObservable()
                                .map(cartListData -> {
                                    updateAndRefreshCartListData.setCartListData(cartListData);
                                    return updateAndRefreshCartListData;
                                })
                )
                .subscribeOn(schedulers.getIo())
                .observeOn(schedulers.getMain());

    }
}
