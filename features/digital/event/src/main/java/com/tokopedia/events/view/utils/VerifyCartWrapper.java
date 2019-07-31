package com.tokopedia.events.view.utils;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.postusecase.VerifyCartUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by pranaymohapatra on 23/07/18.
 */

public class VerifyCartWrapper {
    private VerifyCartUseCase useCase;

    public VerifyCartWrapper(VerifyCartUseCase usecase) {
        useCase = usecase;
    }

    public Observable<TKPDMapParam<String, Object>> verifyPromo(RequestParams params) {
        return useCase.createObservable(params).subscribeOn(Schedulers.io()).map(new Func1<VerifyCartResponse, TKPDMapParam<String, Object>>() {
            @Override
            public TKPDMapParam<String, Object> call(VerifyCartResponse verifyCartResponse) {
                TKPDMapParam<String, Object> resultMap = new TKPDMapParam<>();
                resultMap.put(Utils.Constants.PROMOCODE, verifyCartResponse.getCart().getPromocode());
                resultMap.put(Utils.Constants.PROMOCODE_DISCOUNT, verifyCartResponse.getCart().getPromocodeDiscount());
                resultMap.put(Utils.Constants.PROMO_CASHBACK, verifyCartResponse.getCart().getPromocodeCashback());
                resultMap.put(Utils.Constants.PROMO_FAILURE, verifyCartResponse.getCart().getPromocodeFailureMessage());
                resultMap.put(Utils.Constants.PROMO_SUCCESS, verifyCartResponse.getCart().getPromocodeSuccessMessage());
                resultMap.put(Utils.Constants.PROMO_STATUS, verifyCartResponse.getCart().getPromocodeStatus());
                return resultMap;
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }
}
