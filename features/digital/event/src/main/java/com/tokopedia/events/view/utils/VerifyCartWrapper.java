package com.tokopedia.events.view.utils;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.postusecase.VerifyCartUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by pranaymohapatra on 23/07/18.
 */

public class VerifyCartWrapper {
    private VerifyCartUseCase useCase;

    public VerifyCartWrapper(VerifyCartUseCase usecase) {
        useCase = usecase;
    }

    public Observable<TKPDMapParam<String, Object>> verifyPromo(RequestParams params) {
        return useCase.createObservable(params).map(new Func1<VerifyCartResponse, TKPDMapParam<String, Object>>() {
            @Override
            public TKPDMapParam<String, Object> call(VerifyCartResponse verifyCartResponse) {
                TKPDMapParam<String, Object> resultMap = new TKPDMapParam<>();
                resultMap.put("promocode", verifyCartResponse.getCart().getPromocode());
                resultMap.put("promocode_discount", verifyCartResponse.getCart().getPromocodeDiscount());
                resultMap.put("promocode_cashback", verifyCartResponse.getCart().getPromocodeCashback());
                resultMap.put("promocode_failure_message", verifyCartResponse.getCart().getPromocodeFailureMessage());
                resultMap.put("promocode_success_message", verifyCartResponse.getCart().getPromocodeSuccessMessage());
                resultMap.put("promocode_status", verifyCartResponse.getCart().getPromocodeStatus());
                return resultMap;
            }
        });
    }
}
