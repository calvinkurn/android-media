package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.cartlist.ResetCartData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.transactiondata.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class ResetCartUseCase extends UseCase<ResetCartData> {

    public static final String PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART
            = "PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART";

    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;

    @Inject
    public ResetCartUseCase(ICartRepository cartRepository, ICartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<ResetCartData> createObservable(RequestParams requestParams) {

        final TKPDMapParam<String, String> paramReset = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_RESET_CART);

        return cartRepository.resetCart(paramReset).map(new Func1<ResetCartDataResponse, ResetCartData>() {
            @Override
            public ResetCartData call(ResetCartDataResponse resetCartDataResponse) {
                return cartMapper.convertToResetCartData(resetCartDataResponse);
            }
        });
    }
}
