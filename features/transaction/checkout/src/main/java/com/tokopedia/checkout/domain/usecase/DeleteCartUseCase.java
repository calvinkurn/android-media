package com.tokopedia.checkout.domain.usecase;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.checkout.domain.mapper.ICartMapper;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 30/04/18.
 */
public class DeleteCartUseCase extends UseCase<DeleteCartData> {
    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";
    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;

    @Inject
    public DeleteCartUseCase(ICartRepository cartRepository, ICartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<DeleteCartData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING);
        return cartRepository.deleteCartData(param)
                .map(new Func1<DeleteCartDataResponse, DeleteCartData>() {
                    @Override
                    public DeleteCartData call(DeleteCartDataResponse deleteCartDataResponse) {
                        return cartMapper.convertToDeleteCartData(deleteCartDataResponse);
                    }
                });
    }
}
