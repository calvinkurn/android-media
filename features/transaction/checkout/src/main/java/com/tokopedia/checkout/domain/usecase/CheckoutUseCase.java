package com.tokopedia.checkout.domain.usecase;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.transactiondata.entity.request.CheckoutRequest;
import com.tokopedia.transactiondata.entity.response.checkout.CheckoutDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public class CheckoutUseCase extends UseCase<CheckoutData> {
    public static final String PARAM_CARTS = "carts";

    private final ICartRepository cartRepository;
    private final ICheckoutMapper checkoutMapper;

    @Inject
    public CheckoutUseCase(ICartRepository cartRepository,
                           ICheckoutMapper checkoutMapper) {
        this.cartRepository = cartRepository;
        this.checkoutMapper = checkoutMapper;
    }

    @Override
    public Observable<CheckoutData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        CheckoutRequest checkoutRequest = (CheckoutRequest) requestParams.getObject(PARAM_CARTS);
        param.put(PARAM_CARTS, new Gson().toJson(checkoutRequest));
        param.put("optional", "0");
        return cartRepository.checkout(param)
                .map(new Func1<CheckoutDataResponse, CheckoutData>() {
                    @Override
                    public CheckoutData call(CheckoutDataResponse checkoutDataResponse) {
                        return checkoutMapper.convertCheckoutData(checkoutDataResponse);
                    }
                });
    }
}
